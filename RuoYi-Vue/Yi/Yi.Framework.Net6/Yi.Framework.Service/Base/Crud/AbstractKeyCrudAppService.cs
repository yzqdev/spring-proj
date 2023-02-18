using AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Interface.Base.Crud;
using Yi.Framework.Model.Base;
using Yi.Framework.Repository;

namespace Yi.Framework.Service.Base.Crud
{
    public abstract class AbstractKeyCrudAppService<TEntity, TEntityDto, TKey>
    : AbstractKeyCrudAppService<TEntity, TEntityDto, TKey, TEntityDto, TEntityDto>
    where TEntity : class, IEntity, new()
    {
    }

    public abstract class AbstractKeyCrudAppService<TEntity, TEntityDto, TKey, TCreateUpdateInput>
        : AbstractKeyCrudAppService<TEntity, TEntityDto, TKey, TCreateUpdateInput, TCreateUpdateInput>
        where TEntity : class, IEntity, new()
    {
    }

    public abstract class AbstractKeyCrudAppService<TEntity, TEntityDto, TKey, TCreateInputDto, TUpdateInputDto>
        : AbstractKeyCrudAppService<TEntity, TEntityDto, TEntityDto, TKey, TCreateInputDto, TUpdateInputDto>
        where TEntity : class, IEntity, new()
    {
        protected override Task<TEntityDto> MapToGetListOutputDtoAsync(TEntity entity)
        {
            return MapToGetOutputDtoAsync(entity);
        }

        protected override TEntityDto MapToGetListOutputDto(TEntity entity)
        {
            return MapToGetOutputDto(entity);
        }
    }

    public abstract class AbstractKeyCrudAppService<TEntity, TGetOutputDto, TListOutputDto, TKey, TCreateInputDto, TUpdateInputDto>
           : AbstractKeyReadOnlyAppService<TEntity, TGetOutputDto, TListOutputDto, TKey>,
               ICrudAppService<TGetOutputDto, TListOutputDto, TKey, TCreateInputDto, TUpdateInputDto>
           where TEntity : class, IEntity, new()
    {

        /// <summary>
        /// 创建
        /// </summary>
        /// <param name="dto"></param>
        /// <returns></returns>
        public virtual async Task<TGetOutputDto> CreateAsync(TCreateInputDto input)
        {
        
            var entity = await MapToEntityAsync(input);

            TryToSetTenantId(entity);

            //这边需要进行判断，实体是什么guid还是雪花id
            await Repository.InsertReturnSnowflakeIdAsync(entity);

            var entitydto = await MapToGetOutputDtoAsync(entity);
            return entitydto;
        }


        public async virtual Task CreateAsync(IEnumerable<TCreateInputDto> dtos)
        {

            var entity = await MapToEntitysAsync(dtos);

            TryToSetTenantId(entity);

            //这边需要进行判断，实体是什么guid还是雪花id
            await Repository.InsertReturnSnowflakeIdAsync(entity);
        }

        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="ids"></param>
        /// <returns></returns>
        /// <exception cref="NotImplementedException"></exception>
        public virtual Task DeleteAsync(IEnumerable<TKey> ids)
        {
            throw new NotImplementedException();
        }
        protected abstract Task DeleteByIdAsync(TKey id);

        /// <summary>
        /// 更新
        /// </summary>
        /// <param name="id"></param>
        /// <param name="dto"></param>
        /// <returns></returns>
        public virtual async Task<TGetOutputDto> UpdateAsync(TKey id, TUpdateInputDto input)
        {
            var entity = await GetEntityByIdAsync(id);

            await UpdateValidAsync(entity, input);
            //TODO: Check if input has id different than given id and normalize if it's default value, throw ex otherwise
            await MapToEntityAsync(input, entity);
            await Repository.UpdateAsync(entity);

            var entitydto = await MapToGetOutputDtoAsync(entity);
            return entitydto;
        }
        /// <summary>
        /// 效验更新
        /// </summary>
        /// <param name="idEntity"></param>
        /// <param name="dto"></param>
        /// <returns></returns>
        protected virtual Task UpdateValidAsync(TEntity idEntity, TUpdateInputDto dto)
        {
            return Task.CompletedTask;
        }

        /// <summary>
        ///  将 批量更新输入dto转化为实体的同步方法
        /// </summary>
        /// <param name="updateInput"></param>
        /// <returns></returns>
        protected virtual List<TEntity> MapToEntity(IEnumerable<TCreateInputDto> updateInput)
        {
            return ObjectMapper.Map<List<TEntity>>(updateInput);
        }


        /// <summary>
        /// 将 批量更新输入dto转化为实体的异步
        /// </summary>
        /// <param name="updateInput"></param>
        /// <param name="entity"></param>
        protected virtual async Task<List<TEntity>> MapToEntitysAsync(IEnumerable<TCreateInputDto> updateInput)
        {
            List<TEntity> entitys = MapToEntity(updateInput);
            return await Task.FromResult(entitys);
        }

        /// <summary>
        /// 将 更新输入dto转化为实体的同步方法
        /// </summary>
        /// <param name="updateInput"></param>
        /// <param name="entity"></param>
        protected virtual void MapToEntity(TUpdateInputDto updateInput, TEntity entity)
        {
            ObjectMapper.Map(updateInput, entity);
        }


        /// <summary>
        /// 将 更新输入dto转化为实体的异步
        /// </summary>
        /// <param name="updateInput"></param>
        /// <param name="entity"></param>
        protected virtual Task MapToEntityAsync(TUpdateInputDto updateInput, TEntity entity)
        {
            MapToEntity(updateInput, entity);
            return Task.CompletedTask;
        }
        /// <summary>
        /// 创建dto 给 实体的转换的异步方法
        /// </summary>
        /// <param name="createInput"></param>
        /// <returns></returns>
        protected virtual Task<TEntity> MapToEntityAsync(TCreateInputDto createInput)
        {
            return Task.FromResult(MapToEntity(createInput));
        }

        /// <summary>
        /// 创建dto 给 实体的转换
        /// </summary>
        /// <param name="createInput"></param>
        /// <returns></returns>
        protected virtual TEntity MapToEntity(TCreateInputDto createInput)
        {
            var entity = ObjectMapper.Map<TCreateInputDto, TEntity>(createInput);
            SetIdForGuids(entity);
            return entity;
        }

        /// <summary>
        /// 给主键id赋值上guid
        /// </summary>
        /// <param name="entity"></param>
        protected virtual void SetIdForGuids(TEntity entity)
        {
            if (entity is IEntity<Guid> entityWithGuidId && entityWithGuidId.Id == Guid.Empty)
            {
                //这里给主键赋值为guid,l临时写死属性名
                entity.GetType().GetProperty("Id").SetValue(entity, Guid.NewGuid());
                //EntityHelper.TrySetId(
                //    entityWithGuidId,
                //    () => GuidGenerator.Create(),
                //    true
                //);
            }
        }

        /// <summary>
        /// 给租户id赋值
        /// </summary>
        /// <param name="entity"></param>
        protected virtual void TryToSetTenantId(TEntity entity)
        {
            //实现多租户接口
            //if (entity is IMultiTenant)
            //{
            //    //给属性租户id赋值
            //    if (ServiceLocator.GetTenantId(out var tid))
            //    {
            //        var tenantId = tid;

            //        var propertyInfo = entity.GetType().GetProperty(nameof(IMultiTenant.TenantId));

            //        if (propertyInfo == null || propertyInfo.GetSetMethod(true) == null)
            //        {
            //            return;
            //        }

            //        propertyInfo.SetValue(entity, tenantId);
            //    }

            //}
        }

        protected virtual void TryToSetTenantId(IEnumerable<TEntity> entitys)
        {
            foreach (var entity in entitys)
            {
                TryToSetTenantId(entity);
            }

        }


        /// <summary>
        /// 判断租户id的属性是否为空
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        protected virtual bool HasTenantIdProperty(TEntity entity)
        {
            return entity.GetType().GetProperty(nameof(IMultiTenant.TenantId)) != null;
        }
    }
}
