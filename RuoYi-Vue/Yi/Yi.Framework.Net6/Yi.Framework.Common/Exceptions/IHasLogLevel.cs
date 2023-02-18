using Microsoft.Extensions.Logging;

namespace Yi.Framework.Common.Exceptions;

/// <summary>
/// ����<see cref="LogLevel"/> ���ԵĽӿ�
/// </summary>
public interface IHasLogLevel
{
    /// <summary>
    /// Log severity.
    /// </summary>
    LogLevel LogLevel { get; set; }
}
