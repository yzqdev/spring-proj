using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;
using System;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.Common.Models;
using Yi.Framework.Core;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// 通用跨域扩展
    /// </summary>
    public static class JwtExtension
    {
        public static IServiceCollection AddJwtService(this IServiceCollection services)
        {
            services.Configure<JWTTokenOptions>(Appsettings.appConfiguration("JwtAuthorize"));
            services.AddTransient<JwtInvoker>();
            var jwtOptions = Appsettings.app<JWTTokenOptions>("JwtAuthorize");
            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
                             .AddJwtBearer(options =>
                             {
                                 options.Events = new JwtBearerEvents
                                 {
                                     OnAuthenticationFailed = (context) =>
                                     {
                                         return Task.CompletedTask;
                                     },
                                     OnMessageReceived = (context) =>
                                     {
                                         return Task.CompletedTask;
                                     },
                                     OnChallenge = (context) =>
                                     {
                                         return Task.CompletedTask;
                                     },
                                 };

                                 options.TokenValidationParameters = new TokenValidationParameters
                                 {
                                     ClockSkew = TimeSpan.Zero,//过期缓冲时间
                                     ValidateIssuer = true,//是否验证Issuer
                                     ValidateAudience = true,//是否验证Audience
                                     ValidateLifetime = true,//是否验证失效时间
                                     ValidateIssuerSigningKey = true,//是否验证SecurityKey
                                     ValidAudience = jwtOptions.Audience,//Audience
                                     ValidIssuer = jwtOptions.Issuer,//Issuer，这两项和前面签发jwt的设置一致
                                     IssuerSigningKey = new RsaSecurityKey(RSAFileHelper.GetPublicKey())
                                 };
                             });
            return services;
        }
    }
}
