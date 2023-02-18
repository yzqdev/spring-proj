using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Enum;
using Yi.Framework.Model.Base;

namespace Yi.Framework.DtoModel.ERP.Warehouse
{
    public class WarehouseGetListInput
    {
        public string Code { get; set; }=string.Empty;
        public string Name { get; set; }=string.Empty ;
        public string? Remarks { get; set; }
        public StateEnum State { get; set; } = StateEnum.Normal;
    }
}
