using System;
using System.Runtime.ExceptionServices;
using Microsoft.Extensions.Logging;
using Yi.Framework.Common.Enum;

namespace Yi.Framework.Common.Exceptions;

/// <summary>
/// <see cref="Exception"/>�����չ����
/// </summary>
public static class ExceptionExtensions
{
    /// <summary>
    /// ʹ�� <see cref="ExceptionDispatchInfo.Capture"/> �ٴ��׳��쳣
    /// </summary>
    /// <param name="exception">Exception to be re-thrown</param>
    public static void ReThrow(this Exception exception)
    {
        ExceptionDispatchInfo.Capture(exception).Throw();
    }

    /// <summary>
    /// ��ȡ�쳣�е���־�ȼ�
    /// </summary>
    /// <param name="exception"></param>
    /// <param name="defaultLevel"></param>
    /// <returns></returns>
    public static LogLevel GetLogLevel(this Exception exception, LogLevel defaultLevel = LogLevel.Error)
    {
        return (exception as IHasLogLevel)?.LogLevel ?? defaultLevel;
    }
    /// <summary>
    /// ��ȡ�쳣�е���־������
    /// </summary>
    /// <param name="exception"></param>
    /// <param name="defaultLevel"></param>
    /// <returns></returns>
    public static ResultCodeEnum GetLogErrorCode(this Exception exception, ResultCodeEnum defaultCode = ResultCodeEnum.NotSuccess)
    {
        return (exception as IHasErrorCode)?.Code ?? defaultCode;
    }
}
