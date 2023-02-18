using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Models
{

    public class PageParModel
    {
        public int PageNum { get; set; }
        public int PageSize { get; set; }

        public DateTime? StartTime {get;set;}
        public DateTime? EndTime { get; set; }
    }
}
