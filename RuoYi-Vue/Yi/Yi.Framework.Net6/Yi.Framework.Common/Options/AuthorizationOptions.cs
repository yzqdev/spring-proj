using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.IOCOptions
{
    public class AuthorizationOptions
    {
        public string Refresh { get; set; }
        public List<string> WhiteList { get; set; }
        public List<string> AccountList { get; set; }
        public List<string> UserList { get; set; }

        public List<string> TenantList { get; set; }
    }
}
