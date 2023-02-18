using System;
using System.Runtime.Serialization;
using Microsoft.Extensions.Logging;
using Yi.Framework.Common.Enum;

namespace Yi.Framework.Common.Exceptions;

/// <summary>
/// 用户友好异常
/// </summary>
[Serializable]
public class UserFriendlyException : BusinessException
{
    public UserFriendlyException(
        string message,
        ResultCodeEnum code = ResultCodeEnum.NotSuccess,
        string? details = null,
        Exception? innerException = null,
        LogLevel logLevel = LogLevel.Warning)
        : base(
              code,
              message,
              details,
              innerException,
              logLevel)
    {
        Details = details;
    }

    /// <summary>
    /// 序列化参数的构造函数
    /// </summary>
    public UserFriendlyException(SerializationInfo serializationInfo, StreamingContext context)
        : base(serializationInfo, context)
    {

    }
}
