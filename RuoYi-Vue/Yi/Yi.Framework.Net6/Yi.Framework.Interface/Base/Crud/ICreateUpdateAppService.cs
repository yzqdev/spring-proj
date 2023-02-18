using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Interface.Base.Crud
{
    public interface ICreateUpdateAppService<TEntityDto, in TKey>
           : ICreateUpdateAppService<TEntityDto, TKey, TEntityDto, TEntityDto>
    {

    }

    public interface ICreateUpdateAppService<TEntityDto, in TKey, in TCreateUpdateInput>
        : ICreateUpdateAppService<TEntityDto, TKey, TCreateUpdateInput, TCreateUpdateInput>
    {

    }

    public interface ICreateUpdateAppService<TGetOutputDto, in TKey, in TCreateUpdateInput, in TUpdateInput>
        : ICreateAppService<TGetOutputDto, TCreateUpdateInput>,
            IUpdateAppService<TGetOutputDto, TKey, TUpdateInput>
    {

    }
}
