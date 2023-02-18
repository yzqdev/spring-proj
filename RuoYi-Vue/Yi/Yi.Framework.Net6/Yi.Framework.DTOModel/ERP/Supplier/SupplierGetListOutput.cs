using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.Base;

namespace Yi.Framework.DtoModel.ERP.Supplier
{
    public class SupplierGetListOutput: EntityDto<long>
    {
        public string Code { get; set; } = string.Empty;
        public string Name { get; set; }= string.Empty;
        public string? Address { get; set; }
        public long? Phone { get; set; }
        public string? Fax { get; set; }
        public string? Email { get; set; }
    }
}
