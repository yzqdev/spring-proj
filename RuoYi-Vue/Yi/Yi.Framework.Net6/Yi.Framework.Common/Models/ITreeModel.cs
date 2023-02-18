using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Models
{
    public interface ITreeModel<T>
    {
        public long Id { get; set; }
        public long ParentId { get; set; }
        public int OrderNum { get; set; }

        public List<T>? Children { get; set; }
    }
}
