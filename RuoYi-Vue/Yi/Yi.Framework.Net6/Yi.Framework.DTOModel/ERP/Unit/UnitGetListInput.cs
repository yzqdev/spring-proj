using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.Base;

namespace Yi.Framework.DtoModel.ERP.Unit
{
    public class UnitGetListInput
    {
        public string Code { get; set; }=string.Empty;
        public string Name { get; set; }=string.Empty;
        public string? Remarks { get; set; }
    }
}
