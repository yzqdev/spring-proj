using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Interface.Base.Crud
{
    public interface ICrudAppService<TEntityDto, in TKey>
           : ICrudAppService<TEntityDto, TKey, TEntityDto>
    {

    }

    public interface ICrudAppService<TEntityDto, in TKey, in TCreateInputDto>
        : ICrudAppService<TEntityDto, TKey, TCreateInputDto, TCreateInputDto>
    {

    }

    public interface ICrudAppService<TEntityDto, in TKey, in TCreateInputDto, in TUpdateInputDto>
        : ICrudAppService<TEntityDto, TEntityDto, TKey, TCreateInputDto, TUpdateInputDto>
    {

    }

    public interface ICrudAppService<TGetOutputDto, TListOutputDto, in TKey, in TCreateInputDto, in TUpdateInputDto>
        : IReadOnlyAppService<TGetOutputDto, TListOutputDto, TKey>,
            ICreateUpdateAppService<TGetOutputDto, TKey, TCreateInputDto, TUpdateInputDto>,
            IDeleteAppService<TKey>
    {

    }
}
