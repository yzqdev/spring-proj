using AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Model.Base;
using Yi.Framework.Repository;

namespace Yi.Framework.Service.Base.Crud
{
    public abstract class CrudAppService<TEntity, TEntityDto, TKey>
      : CrudAppService<TEntity, TEntityDto, TKey, TEntityDto>
      where TEntity : class, IEntity<TKey>, new()
    {
    }

    public abstract class CrudAppService<TEntity, TEntityDto, TKey, TCreateInputDto>
        : CrudAppService<TEntity, TEntityDto, TKey, TCreateInputDto, TCreateInputDto>
        where TEntity : class, IEntity<TKey>, new()
    {
    }

    public abstract class CrudAppService<TEntity, TEntityDto, TKey, TCreateInputDto, TUpdateInputDto>: CrudAppService<TEntity, TEntityDto, TEntityDto, TKey, TCreateInputDto, TUpdateInputDto>
        where TEntity : class, IEntity<TKey>, new()
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

    public abstract class CrudAppService<TEntity, TGetOutputDto, TListOutputDto, TKey, TCreateInputDto, TUpdateInputDto>
         : AbstractKeyCrudAppService<TEntity, TGetOutputDto, TListOutputDto, TKey, TCreateInputDto, TUpdateInputDto>
         where TEntity : class, IEntity<TKey>, new()
    {
        protected override async Task DeleteByIdAsync(TKey id)
        {
            await DeleteAsync(new List<TKey> { id });
        }

        protected override async Task<TEntity> GetEntityByIdAsync(TKey id)
        {
            return await Repository.GetByIdAsync(id);
        }

        protected override void MapToEntity(TUpdateInputDto updateInput, TEntity entity)
        {
            if (updateInput is IEntityDto<TKey> entityDto)
            {
                entityDto.Id = entity.Id;
            }

            base.MapToEntity(updateInput, entity);
        }


        public override async Task<Result<bool>> DeleteAsync(IEnumerable<TKey> ids)
        {
            await Repository.DeleteAsync(e => ids.Contains(e.Id));
            return Result<bool>.Success();
        }

    }
}
