using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.WebCore.LogExtend
{
    public class CustomLoggerProvider : ILoggerProvider
    {
        //创建我们自定义日志的方法
        public ILogger CreateLogger(string categoryName)
        {
            return new CustomLogger();
        }

        public void Dispose()
        {
        }
    }
}
