using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.OpenApi.Models;
using System;
using System.IO;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.Core.Cache;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// Redis扩展
    /// </summary>
    public static class CacheExtension
    {
        public static IServiceCollection AddCacheService(this IServiceCollection services)
        {
            var cacheSelect = Appsettings.app("CacheSelect");

            switch (cacheSelect)
            {
                case "Redis":
                    services.Configure<RedisConnOptions>(Appsettings.appConfiguration("RedisConnOptions"));
                    services.AddSingleton<CacheInvoker, RedisCacheClient>();
                    break;
                case "MemoryCache":
                    services.AddSingleton<CacheInvoker, MemoryCacheClient>();
                    break;
                default: throw new ArgumentException("CacheSelect配置填的是什么东西？俺不认得");
            }
            return services;
        }
    }
}
