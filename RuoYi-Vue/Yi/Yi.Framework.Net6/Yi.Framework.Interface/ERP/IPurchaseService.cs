using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Purchase;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Interface.ERP
{
    public interface IPurchaseService : ICrudAppService<PurchaseGetListOutput, long, PurchaseCreateInput, PurchaseUpdateInput>
    {
        Task<PageModel<List<PurchaseGetListOutput>>> PageListAsync(PurchaseGetListInput input, PageParModel page);
    }
}
