using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Supplier;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Interface.ERP
{
    public interface ISupplierService : ICrudAppService<SupplierGetListOutput, long, SupplierCreateUpdateInput>
    {
        Task<PageModel<List<SupplierGetListOutput>>> PageListAsync(SupplierCreateUpdateInput input, PageParModel page);
    }
}
