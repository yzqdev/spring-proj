using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Warehouse;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Interface.ERP
{
    public interface IWarehouseService : ICrudAppService<WarehouseGetListOutput, long, WarehouseCreateUpdateInput>
    {
        Task<PageModel<List<WarehouseGetListOutput>>> PageListAsync(WarehouseCreateUpdateInput input, PageParModel page);
    }
}
