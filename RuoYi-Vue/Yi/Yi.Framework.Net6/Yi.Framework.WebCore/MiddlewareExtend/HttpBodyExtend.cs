using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.WebCore.MiddlewareExtend
{
    public class HttpBodyMiddleware
    {

        private readonly RequestDelegate _next;
        private ILogger<HttpBodyMiddleware> _logger;
        public HttpBodyMiddleware(RequestDelegate next, ILogger<HttpBodyMiddleware> logger)
        {
            _next = next;
            _logger = logger;
        }

        public async Task Invoke(HttpContext context)
        {

            context.Request.EnableBuffering();
            if (context.Request.Query.TryGetValue("access_token", out var token))
            {
                context.Request.Headers.Add("Authorization", $"Bearer {token}");
            }
            await _next(context);

        }

    }

    public static class HttpBodyExtend
    {
        public static IApplicationBuilder UseHttpBodyService(this IApplicationBuilder builder)
        {
            return builder.UseMiddleware<HttpBodyMiddleware>();
        }
    }
}
