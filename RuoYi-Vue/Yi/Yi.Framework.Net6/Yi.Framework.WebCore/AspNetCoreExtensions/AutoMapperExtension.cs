using AutoMapper;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.IO;
using Yi.Framework.WebCore.Mapper;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// 通用autoMapper扩展
    /// </summary>
    public static class AutoMapperExtension
    {
        public static IServiceCollection AddAutoMapperService(this IServiceCollection services)
        {
            //这里会通过反射自动注入的，先临时这样

            var profileList = Common.Helper.AssemblyHelper.GetClassByBaseClassesAndInterfaces("Yi.Framework.DtoModel", typeof(Profile));
            profileList.Add(typeof(AutoMapperProfile));
            services.AddAutoMapper(profileList.ToArray());

            return services;
        }
    }
}
