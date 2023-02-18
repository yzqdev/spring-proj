using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Unit;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Interface.ERP
{
    public interface IUnitService : ICrudAppService<UnitGetListOutput, long, UnitCreateUpdateInput>
    {
        Task<PageModel<List<UnitGetListOutput>>> PageListAsync(UnitCreateUpdateInput input, PageParModel page);
    }
}
