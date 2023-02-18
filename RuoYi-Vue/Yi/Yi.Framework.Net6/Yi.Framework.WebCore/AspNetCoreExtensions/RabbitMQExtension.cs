﻿using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.OpenApi.Models;
using System;
using System.IO;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.Core;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// Redis扩展
    /// </summary>
    public static class RabbitMQExtension
    {
        public static IServiceCollection AddRabbitMQService(this IServiceCollection services)
        {
            if (Appsettings.appBool("RabbitMQ_Enabled"))
            {
                services.Configure<RabbitMQOptions>(Appsettings.appConfiguration("RabbitConn"));
                services.AddTransient<RabbitMQInvoker>();
            }
            return services;

        }
    }
}
