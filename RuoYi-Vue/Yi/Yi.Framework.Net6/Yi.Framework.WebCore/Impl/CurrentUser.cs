using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Abstract;

namespace Yi.Framework.WebCore.Impl
{
    public class CurrentUser : ICurrentUser
    {
        public bool IsAuthenticated { get; set; }

        public long Id { get; set; }

        public string UserName { get; set; } = string.Empty;

        public Guid? TenantId { get; set; }

        public string Email { get; set; }=string.Empty;

        public bool EmailVerified { get; set; }

        public string PhoneNumber { get; set; } = string.Empty;

        public bool PhoneNumberVerified { get; set; }

        public string[]? Roles { get; set; }

        public string[]? Permission { get; set; }
    }
}
