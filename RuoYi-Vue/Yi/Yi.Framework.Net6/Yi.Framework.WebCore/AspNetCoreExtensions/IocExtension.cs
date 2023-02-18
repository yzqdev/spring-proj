using Microsoft.AspNetCore.Builder;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.IO;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.Model;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// 通用跨域扩展
    /// </summary>
    public static class IocExtension
    {
        public static IServiceCollection AddIocService(this IServiceCollection services, IConfiguration configuration)
        {
            #region
            //配置文件使用配置
            #endregion
            services.AddSingleton(new Appsettings(configuration));

            #region
            //数据库连接字符串
            #endregion
            services.Configure<SqlConnOptions>(Appsettings.appConfiguration("DbConn"));

            return services;
        }

    }
}
