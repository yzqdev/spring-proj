using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Models
{
    /// <summary>
    /// 写入分布式日志需要的字段
    /// </summary>
    public class LogModel
    {
        public string OriginalClassName { get; set; } = string.Empty;
        public string OriginalMethodName { get; set; } = string.Empty;
        public string? Remark { get; set; }
    }
}
