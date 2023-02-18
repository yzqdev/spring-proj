using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base
{
    public interface IMultiTenant
    {
        Guid? TenantId { get; }
    }
}
