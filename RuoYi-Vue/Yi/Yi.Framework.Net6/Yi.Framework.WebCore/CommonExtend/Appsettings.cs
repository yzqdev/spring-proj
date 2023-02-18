﻿using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Configuration.Json;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Yi.Framework.WebCore.CommonExtend
{
    /// <summary>
    /// appsettings.json操作类
    /// </summary>
    public class Appsettings
    {
        static IConfiguration? Configuration { get; set; }
        static string? contentPath { get; set; }

        public Appsettings(string contentPath)
        {
            string Path = "appsettings.json";

            //如果你把配置文件 是 根据环境变量来分开了，可以这样写
            //Path = $"appsettings.{Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT")}.json";

            Configuration = new ConfigurationBuilder()
               .SetBasePath(contentPath)
               .Add(new JsonConfigurationSource { Path = Path, Optional = false, ReloadOnChange = true })//这样的话，可以直接读目录里的json文件，而不是 bin 文件夹下的，所以不用修改复制属性
               .Build();
        }

        public Appsettings(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        /// <summary>
        /// 封装要操作的字符
        /// </summary>
        /// <param name="sections">节点配置</param>
        /// <returns></returns>
        public static string? app(params string[] sections)
        {
            try
            {

                if (sections.Any())
                {
                    return Configuration?[string.Join(":", sections)];
                }
            }
            catch (Exception) { }

            return "";
        }

        public static bool appBool(params string[] sections)
        {

            return Bool(app(sections));

        }

        public static bool Bool(object? thisValue)
        {
            bool reval = false;
            if (thisValue != null && thisValue != DBNull.Value && bool.TryParse(thisValue.ToString(), out reval))
            {
                return reval;
            }
            return reval;
        }

        /// <summary>
        /// 递归获取配置信息数组
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="sections"></param>
        /// <returns></returns>
        public static T app<T>(params string[] sections)
        {
            T app = Activator.CreateInstance<T>();
            // 引用 Microsoft.Extensions.Configuration.Binder 包
            Configuration.Bind(string.Join(":", sections), app);
            return app;
        }


        public static IConfiguration? appConfiguration(params string[] sections)
        {
            return Configuration?.GetSection(string.Join(":", sections));
        }
    }
}
