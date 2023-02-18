using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Models
{

    public class PageModel<T>
    {
        public PageModel() { }
        public PageModel(T data,int total)
        { 
            Data = data;
            Total = total;
        }
        public int Total { get; set; }
        public T Data { get; set; } = default(T)!;
    }

    public class PageModel : PageModel<object>
    {
        public PageModel() { }
        public PageModel(object data, int total) : base(data, total)
        {
        }
    }
}
