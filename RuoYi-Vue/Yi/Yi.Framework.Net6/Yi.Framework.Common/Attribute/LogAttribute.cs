using Microsoft.AspNetCore.Mvc.Filters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Enum;

namespace Yi.Framework.Common.Attribute
{
    [AttributeUsage(AttributeTargets.Method)]
    public class LogAttribute : System.Attribute
    {
        /// <summary>
        /// 操作类型
        /// </summary>
        public OperEnum OperType { get; set; }

        /// <summary>
        /// 日志标题（模块）
        /// </summary>
        public string Title { get; set; }

        /// <summary>
        /// 是否保存请求数据
        /// </summary>
        public bool IsSaveRequestData { get; set; } = true;

        /// <summary>
        /// 是否保存返回数据
        /// </summary>
        public bool IsSaveResponseData { get; set; } = true;

        public LogAttribute(string title, OperEnum operationType)
        {
            this.Title = title;
            this.OperType = operationType;
        }
    }
}