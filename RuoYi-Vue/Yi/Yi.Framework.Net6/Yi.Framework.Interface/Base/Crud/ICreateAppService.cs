using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Interface.Base.Crud
{
    public interface ICreateAppService<TEntityDto>: ICreateAppService<TEntityDto, TEntityDto>
    {

    }


    public interface ICreateAppService<TCreateResultOutputDto , in TCreateInputDto> : IApplicationService
    {
        Task<TCreateResultOutputDto> CreateAsync(TCreateInputDto dto);

        Task CreateAsync(IEnumerable<TCreateInputDto> dtos);
    }
}
