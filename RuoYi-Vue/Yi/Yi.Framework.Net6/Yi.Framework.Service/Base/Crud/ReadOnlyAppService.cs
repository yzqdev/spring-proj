using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.Base;
using Yi.Framework.Repository;

namespace Yi.Framework.Service.Base.Crud
{
    public abstract class ReadOnlyAppService<TEntity, TGetOutputDto, TGetListOutputDto, TKey>
           : AbstractKeyReadOnlyAppService<TEntity, TGetOutputDto, TGetListOutputDto, TKey>
           where TEntity : class, IEntity<TKey>, new()
    {
        protected override async Task<TEntity> GetEntityByIdAsync(TKey id)
        {
            return await Repository.GetByIdAsync(id);
        }
    }
}
