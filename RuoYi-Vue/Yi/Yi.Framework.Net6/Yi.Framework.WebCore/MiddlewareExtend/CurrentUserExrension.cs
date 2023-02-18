using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Yi.Framework.Common.Abstract;
using Yi.Framework.Common.Const;
using Yi.Framework.WebCore.Impl;

namespace Yi.Framework.WebCore.MiddlewareExtend
{
    public static class CurrentUserExrension
    {
        public static IServiceCollection AddCurrentUserServer(this IServiceCollection services)
        {
            return services.AddScoped<ICurrentUser, CurrentUser>();
        }


        public static IApplicationBuilder UseCurrentUserServer(this IApplicationBuilder app)
        {
            return app.UseMiddleware<CurrentUserMiddleware>();
        }
    }

    public class CurrentUserMiddleware
    {

        private readonly RequestDelegate _next;
        private ILogger<CurrentUserMiddleware> _logger;
        public CurrentUserMiddleware(RequestDelegate next, ILogger<CurrentUserMiddleware> logger)
        {
            _next = next;
            _logger = logger;
        }

        public async Task Invoke(HttpContext context, ICurrentUser _currentUser)
        {
            var authenticateContext = await context.AuthenticateAsync();
            if (authenticateContext.Principal is null)
            {
                _currentUser.IsAuthenticated = false;
                await _next(context);
                return;
            }
            var claims = authenticateContext.Principal.Claims;
            //通过鉴权之后，开始赋值
            _currentUser.IsAuthenticated = true;
            _currentUser.Id = claims.GetClaim(JwtRegisteredClaimNames.Sid) is null ? 0 : Convert.ToInt64(claims.GetClaim(JwtRegisteredClaimNames.Sid));
            _currentUser.UserName = claims.GetClaim(SystemConst.UserName)??"";
            _currentUser.Permission = claims.GetClaims(SystemConst.PermissionClaim);
            _currentUser.TenantId = claims.GetClaim(SystemConst.TenantId) is null ? null : Guid.Parse(claims.GetClaim(SystemConst.TenantId)!);
            await _next(context);

        }



    }

    public static class ClaimExtension
    {
        public static string? GetClaim(this IEnumerable<Claim> claims, string type)
        {
            return claims.Where(c => c.Type == type).Select(c => c.Value).FirstOrDefault();
        }

        public static string[]? GetClaims(this IEnumerable<Claim> claims, string type)
        {
            return claims.Where(c => c.Type == type).Select(c => c.Value).ToArray();
        }
    }

}
