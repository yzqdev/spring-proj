using Yi.Framework.Common.Enum;

namespace Yi.Framework.Common.Exceptions;

public interface IHasErrorCode
{
    ResultCodeEnum Code { get; }
}
