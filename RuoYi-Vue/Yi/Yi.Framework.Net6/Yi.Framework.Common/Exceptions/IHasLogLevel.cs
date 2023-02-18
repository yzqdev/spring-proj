using Microsoft.Extensions.Logging;

namespace Yi.Framework.Common.Exceptions;

/// <summary>
/// 定义<see cref="LogLevel"/> 属性的接口
/// </summary>
public interface IHasLogLevel
{
    /// <summary>
    /// Log severity.
    /// </summary>
    LogLevel LogLevel { get; set; }
}
