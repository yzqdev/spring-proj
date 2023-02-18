using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Abstract
{
    public interface ICurrentUser
    {
        public bool IsAuthenticated { get; set; }
        public long Id { get; set; }

        public string UserName { get; set; }

        public Guid? TenantId { get; set; }

        public string Email { get; set; }

        public bool EmailVerified { get; set; }

        public string PhoneNumber { get; set; }

        public bool PhoneNumberVerified { get; set; }

        public string[]? Roles { get; set; }

        public string[]? Permission { get; set; }

    }
}
