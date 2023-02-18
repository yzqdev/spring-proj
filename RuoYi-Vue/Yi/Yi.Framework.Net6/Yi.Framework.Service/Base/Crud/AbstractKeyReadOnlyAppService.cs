using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Security.Principal;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Exceptions;
using Yi.Framework.Interface.Base.Crud;
using Yi.Framework.Model.Base;
using Yi.Framework.Repository;

namespace Yi.Framework.Service.Base.Crud
{
    public abstract class AbstractKeyReadOnlyAppService<TEntity, TEntityDto, TKey>
    : AbstractKeyReadOnlyAppService<TEntity, TEntityDto, TEntityDto, TKey>
    where TEntity : class, IEntity, new()
    {
    }


    public abstract class AbstractKeyReadOnlyAppService<TEntity, TGetOutputDto, TGetListOutputDto, TKey> : ApplicationService,
        IReadOnlyAppService<TGetOutputDto, TGetListOutputDto, TKey>
         where TEntity : class, IEntity, new()
    {
        [Autowired]
        public IRepository<TEntity> Repository { get; set; }


        public async Task<List<TGetListOutputDto>> GetListAsync()
        {
            var entitys = await Repository.GetListAsync();
            var entityDtos = await MapToGetListOutputDtosAsync(entitys);
            return entityDtos;
        }

        public async Task<TGetOutputDto> GetByIdAsync(TKey id)
        {
            var entity = await GetEntityByIdAsync(id);
            if (entity is null)
            {
                throw new UserFriendlyException($"主键：{id} 数据不存在",ResultCodeEnum.NotSuccess);
            }
            var entityDto = await MapToGetOutputDtoAsync(entity);

            return entityDto;
        }

        /// <summary>
        /// 通过id获取实体
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        protected abstract Task<TEntity> GetEntityByIdAsync(TKey id);
        /// <summary>
        /// 实体向Get输出映射的异步方法
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        protected virtual Task<TGetOutputDto> MapToGetOutputDtoAsync(TEntity entity)
        {
            return Task.FromResult(MapToGetOutputDto(entity));
        }

        /// <summary>
        /// 实体向Get输出Dto映射的同步方法
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        protected virtual TGetOutputDto MapToGetOutputDto(TEntity entity)
        {
            return ObjectMapper.Map<TEntity, TGetOutputDto>(entity);
        }
        /// <summary>
        /// 多个实体列表映射GetList输出dto列表的异步方法
        /// </summary>
        /// <param name="entities"></param>
        /// <returns></returns>
        protected virtual async Task<List<TGetListOutputDto>> MapToGetListOutputDtosAsync(IEnumerable<TEntity> entities)
        {
            var dtos = new List<TGetListOutputDto>();

            foreach (var entity in entities)
            {
                dtos.Add(await MapToGetListOutputDtoAsync(entity));
            }

            return dtos;
        }

        /// <summary>
        /// 多个实体列表映射GetList输出dto列表的同步方法
        /// </summary>
        /// <param name="entities"></param>
        /// <returns></returns>
        protected virtual async Task<List<TGetListOutputDto>> MapToGetListOutputDtos(IEnumerable<TEntity> entities)
        {
            var dtos = new List<TGetListOutputDto>();

            foreach (var entity in entities)
            {
                dtos.Add(await MapToGetListOutputDtoAsync(entity));
            }

            return dtos;
        }

        /// <summary>
        /// 实体列表映射GetList输出dto的异步方法
        /// </summary>
        /// <param name="entities"></param>
        /// <returns></returns>
        protected virtual Task<TGetListOutputDto> MapToGetListOutputDtoAsync(TEntity entity)
        {
            return Task.FromResult(MapToGetListOutputDto(entity));
        }
        /// <summary>
        /// 实体列表映射GetList输出dto的同步方法
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        protected virtual TGetListOutputDto MapToGetListOutputDto(TEntity entity)
        {
            return ObjectMapper.Map<TEntity, TGetListOutputDto>(entity);
        }
    }
}
