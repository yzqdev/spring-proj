using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.PurchaseDetails;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Interface.ERP
{
    public interface IPurchaseDetailsService : ICrudAppService<PurchaseDetailsGetListOutput, long, PurchaseDetailsCreateUpdateInput>
    {
        Task<PageModel<List<PurchaseDetailsGetListOutput>>> PageListAsync(PurchaseDetailsCreateUpdateInput input, PageParModel page);
    }
}
