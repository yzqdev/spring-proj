using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.Base;
using Yi.Framework.Model.ERP.Entitys;

namespace Yi.Framework.DtoModel.ERP.Purchase
{
    public class PurchaseGetListInput 
    {
        public string? Code { get; set; }
        public DateTime? NeedTime { get; set; }
        public string? Buyer { get; set; }
        public PurchaseStateEnum? PurchaseState { get; set; }
    }
}
