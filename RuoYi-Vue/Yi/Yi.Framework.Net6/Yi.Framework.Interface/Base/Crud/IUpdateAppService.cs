using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Interface.Base.Crud
{
    public interface IUpdateAppService<TEntityDto, in TKey>
        : IUpdateAppService<TEntityDto, TKey, TEntityDto>
    {

    }
    public interface IUpdateAppService<TUpdateResultOutputDto, in TKey, in TUpdateInputDto> : IApplicationService
    {
        Task<TUpdateResultOutputDto> UpdateAsync(TKey id, TUpdateInputDto dto);
    }
}
