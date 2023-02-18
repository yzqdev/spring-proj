using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.WebCore.LogExtend
{
    public class CustomLogger : ILogger
    {
        public IDisposable BeginScope<TState>(TState state) => default!;

        public bool IsEnabled(LogLevel logLevel)
        {
            return logLevel == LogLevel.Warning;
        }

        //真正日志执行的方法
        public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception? exception, Func<TState, Exception?, string> formatter)
        {
            if (!IsEnabled(logLevel))
            {
                return;
            }
            Console.WriteLine($"你好，这里是自定义的日志哦~,{formatter(state, exception)}");
        }
    }
}
