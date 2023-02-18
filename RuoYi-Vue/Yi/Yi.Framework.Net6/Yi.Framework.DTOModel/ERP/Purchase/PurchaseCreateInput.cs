using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.DtoModel.ERP.PurchaseDetails;
using Yi.Framework.Model.Base;
using Yi.Framework.Model.ERP.Entitys;

namespace Yi.Framework.DtoModel.ERP.Purchase
{
    public class PurchaseCreateInput 
    {
        public string Code { get; set; } = string.Empty;
        public DateTime? NeedTime { get; set; }
        public string Buyer { get; set; } = string.Empty;

        public List<PurchaseDetailsCreateUpdateInput>? PurchaseDetails { get; set; }
    }
}
