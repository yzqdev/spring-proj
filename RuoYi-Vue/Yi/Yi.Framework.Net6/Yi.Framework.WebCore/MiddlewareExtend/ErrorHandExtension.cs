using log4net.Core;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Exceptions;
using Yi.Framework.Common.Models;

namespace Yi.Framework.WebCore.MiddlewareExtend
{
    /// <summary>
    /// 异常抓取反馈扩展
    /// </summary>
    public class ErrorHandExtension
    {
        private readonly RequestDelegate next;
        private readonly ILogger<ErrorHandExtension> _logger;
        //private readonly IErrorHandle _errorHandle;
        public ErrorHandExtension(RequestDelegate next, ILoggerFactory loggerFactory /*, IErrorHandle errorHandle*/)
        {
            this.next = next;
            this._logger = loggerFactory.CreateLogger<ErrorHandExtension>();
            //_errorHandle = errorHandle;
        }

        public async Task Invoke(HttpContext context)
        {
            try
            {
                await next(context);
            }
            catch (BusinessException businessEx)
            {
                var statusCode = 200;
                await HandleExceptionAsync(context, statusCode, businessEx.Message, businessEx.Code);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex,$"系统错误:{ex.Message}");
                //await _errorHandle.Invoer(context, ex);
                var statusCode = context.Response.StatusCode;
                context.Response.StatusCode = 500;
                await HandleExceptionAsync(context, statusCode, ex.Message);
            }
            finally
            {
                var statusCode = context.Response.StatusCode;
                var msg = "";

                switch (statusCode)
                {
                    case 401: msg = "未授权"; break;
                    case 403: msg = "未授权"; break;
                    case 404: msg = "未找到服务"; break;
                    case 502: msg = "请求错误"; break;
                }
                if (!string.IsNullOrWhiteSpace(msg))
                {
                    await HandleExceptionAsync(context, statusCode, msg);
                }
            }
        }
        //异常错误信息捕获，将错误信息用Json方式返回
        private static Task HandleExceptionAsync(HttpContext context, int statusCode, string msg, ResultCodeEnum code = ResultCodeEnum.NotSuccess)
        {
            Result result;
            if (statusCode == 401)
            {
                result = Result.UnAuthorize(msg);
            }
            else
            {
                result = Result.Error(msg).SetCode(code);
            }

            context.Response.ContentType = "application/json;charset=utf-8";
            return context.Response.WriteAsync(JsonConvert.SerializeObject(result));
        }
    }
    //扩展方法
    public static class ErrorHandlingExtensions
    {
        public static IApplicationBuilder UseErrorHandlingService(this IApplicationBuilder builder)
        {
            return builder.UseMiddleware<ErrorHandExtension>();
        }
    }
}