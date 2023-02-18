using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Yi.Framework.Common.IOCOptions
{
    public class JWTTokenOptions
    {
        public string Audience { get; set; }

        public string Issuer { get; set; }

        public string DefaultScheme { get; set; }
        public int Expiration { get; set; }

        public int ReExpiration { get; set; }
    }
}
