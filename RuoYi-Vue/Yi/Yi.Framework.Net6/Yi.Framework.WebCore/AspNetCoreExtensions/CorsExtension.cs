﻿using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.IO;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// 通用跨域扩展
    /// </summary>
    public static class CorsExtension
    {
        public static IServiceCollection AddCorsService(this IServiceCollection services)
        {

            if (Appsettings.appBool("Cors_Enabled"))
            {
                services.AddCors(options => options.AddPolicy("CorsPolicy",//解决跨域问题
                builder =>
                {
                    builder.AllowAnyMethod()
                   .SetIsOriginAllowed(_ => true)
                   .AllowAnyHeader()
                   .AllowCredentials();
                }));
            }
            return services;
        }

        public static void UseCorsService(this IApplicationBuilder app)
        {
            if (Appsettings.appBool("Cors_Enabled"))
            {
                app.UseCors("CorsPolicy");
            }
        }

    }
}
