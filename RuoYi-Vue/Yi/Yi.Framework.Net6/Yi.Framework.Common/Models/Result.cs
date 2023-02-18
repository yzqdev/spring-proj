using Microsoft.Extensions.Localization;
using Yi.Framework.Common.Enum;
using Yi.Framework.Language;

namespace Yi.Framework.Common.Models
{
    public class Result
    {
        public static IStringLocalizer<LocalLanguage>? _local;
        public ResultCodeEnum Code { get; set; }

        public bool Status { get; set; }
        public string? Message { get; set; }
        public object? Data { get; set; }
        public static Result Expire(ResultCodeEnum Code, string msg="")
        {
            return new Result() {  Code = Code, Status=false,  Message = Get(msg, "token_expiration") };
        }
        public static Result Error(string msg = "")
        {
            return new Result() { Code = ResultCodeEnum.NotSuccess, Status = false, Message = Get(msg, "fail") };
        }
        public static Result Success(string msg = "")
        {
            return new Result() { Code = ResultCodeEnum.Success, Status = true, Message = Get( msg, "succeed" )};
        }
        public static Result SuccessError(string msg = "")
        {
            return new Result() { Code = ResultCodeEnum.Success, Status = false, Message = Get(msg, "fail") };
        }


        public static Result UnAuthorize(string msg = "")
        {
            return new Result() { Code = ResultCodeEnum.NoPermission, Status = false, Message = Get(msg, "unAuthorize") };
        }
        public Result SetStatus(bool _Status)
        {
            if (_Status)
            {
                this.Code = ResultCodeEnum.Success;
                this.Message = "操作成功";
            }
            else
            {
                this.Code  = ResultCodeEnum.NotSuccess;
                this.Message = "操作失败";
            }
            this.Status = _Status;
            return this;
        }
        public Result SetData(object? obj)
        {
            this.Data = obj;
            return this;
        }
        public Result SetCode(ResultCodeEnum Code)
        {
            this.Code = Code;
            return this;
        }
        public Result StatusFalse()
        {
            this.Status = false;
            return this;
        }
        public Result StatusTrue()
        {
            this.Status = true;
            return this;
        }

        public static string Get(string msg,string msg2)
        {
            if (msg=="")
            {
                if (_local is not null)
                {
                    msg = _local[msg2];
                }
            }
            return msg;
        }
    }
    public class Result<T>
    {
        public ResultCodeEnum Code { get; set; }
        public string? Message { get; set; }
        public T? Data { get; set; }
        public static Result<T> Error(string msg = "fail")
        {
            return new Result<T>() { Code = ResultCodeEnum.NotSuccess, Message = msg };
        }
        public static Result<T> Success(string msg = "succeed")
        {
            return new Result<T>() { Code = ResultCodeEnum.Success, Message = msg };
        }
        public static Result<T> UnAuthorize(string msg = "unAuthorize")
        {
            return new Result<T>() { Code = ResultCodeEnum.NoPermission, Message = msg };
        }

        public Result<T> SetData(T TValue)
        {
            this.Data = TValue;
            return this;
        }

        public Result<T> SetCode(ResultCodeEnum Code)
        {
            this.Code = Code;
            return this;
        }
    }
}