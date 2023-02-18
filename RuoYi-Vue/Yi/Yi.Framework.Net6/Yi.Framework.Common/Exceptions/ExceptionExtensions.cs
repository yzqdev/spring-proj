using System;
using System.Runtime.ExceptionServices;
using Microsoft.Extensions.Logging;
using Yi.Framework.Common.Enum;

namespace Yi.Framework.Common.Exceptions;

/// <summary>
/// <see cref="Exception"/>类的扩展方法
/// </summary>
public static class ExceptionExtensions
{
    /// <summary>
    /// 使用 <see cref="ExceptionDispatchInfo.Capture"/> 再次抛出异常
    /// </summary>
    /// <param name="exception">Exception to be re-thrown</param>
    public static void ReThrow(this Exception exception)
    {
        ExceptionDispatchInfo.Capture(exception).Throw();
    }

    /// <summary>
    /// 获取异常中的日志等级
    /// </summary>
    /// <param name="exception"></param>
    /// <param name="defaultLevel"></param>
    /// <returns></returns>
    public static LogLevel GetLogLevel(this Exception exception, LogLevel defaultLevel = LogLevel.Error)
    {
        return (exception as IHasLogLevel)?.LogLevel ?? defaultLevel;
    }
    /// <summary>
    /// 获取异常中的日志错误码
    /// </summary>
    /// <param name="exception"></param>
    /// <param name="defaultLevel"></param>
    /// <returns></returns>
    public static ResultCodeEnum GetLogErrorCode(this Exception exception, ResultCodeEnum defaultCode = ResultCodeEnum.NotSuccess)
    {
        return (exception as IHasErrorCode)?.Code ?? defaultCode;
    }
}
