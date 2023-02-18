using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using System;
using Ubiety.Dns.Core.Common;

namespace Yi.Framework.WebCore.CommonExtend
{
    public static class ServiceLocator
    {
        public static IServiceProvider? Instance { get; set; }

        //需要兼容不存在http请求的情况
        public static bool GetHttp(out HttpContext? httpContext)
        {
            httpContext = null;
            var httpContextAccessor = Instance?.GetService<IHttpContextAccessor>();
            if (httpContextAccessor is null)
            {
                return false;
            }
            httpContext = httpContextAccessor.HttpContext;
            return true;
        }
    }

}
