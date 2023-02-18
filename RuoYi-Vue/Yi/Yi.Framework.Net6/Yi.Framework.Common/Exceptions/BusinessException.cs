using System;
using System.Runtime.Serialization;
using Microsoft.Extensions.Logging;
using Yi.Framework.Common.Enum;

namespace Yi.Framework.Common.Exceptions;

[Serializable]
public class BusinessException : Exception,
    IHasErrorCode,
    IHasErrorDetails,
    IHasLogLevel
{
    public ResultCodeEnum Code { get; set; }

    public string? Details { get; set; }

    public LogLevel LogLevel { get; set; }

    public BusinessException(
        ResultCodeEnum code = ResultCodeEnum.NotSuccess,
        string? message = null,
        string? details = null,
        Exception? innerException = null,
        LogLevel logLevel = LogLevel.Warning)
        : base(message, innerException)
    {
        Code = code;
        Details = details;
        LogLevel = logLevel;
    }

    /// <summary>
    /// 序列化参数的构造函数
    /// </summary>
    public BusinessException(SerializationInfo serializationInfo, StreamingContext context)
        : base(serializationInfo, context)
    {

    }

    public BusinessException WithData(string name, object value)
    {
        Data[name] = value;
        return this;
    }
}
