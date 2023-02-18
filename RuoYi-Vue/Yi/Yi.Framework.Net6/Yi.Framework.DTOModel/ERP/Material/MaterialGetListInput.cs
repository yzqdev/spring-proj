using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.Base;

namespace Yi.Framework.DtoModel.ERP.Material
{
    public class MaterialGetListInput
    {
        public string Code { get; set; }=string.Empty;
        public string Name { get; set; }=string.Empty;
        public string UnitName { get; set; }=string.Empty;
        public string? Remarks { get; set; }
    }
}
