using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Helper
{
    public static class IdHelper
    {
        public static dynamic[] ToDynamicArray(this IEnumerable<long> ids)
        {
           return ids.Select(id => (dynamic)id).ToArray();
        }
    }
}
