
using Autofac;
using Autofac.Extras.DynamicProxy;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Attribute;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    public static class AutoIocExtend
    {
        private static void RegIoc(ContainerBuilder build, Assembly assembly)
        {
            foreach (var type in assembly.GetTypes())
            {

                var serviceAttribute = type.GetCustomAttribute<AppServiceAttribute>();
                if (serviceAttribute is not null)
                {
                    //情况1：使用自定义[AppService(ServiceType = typeof(注册抽象或者接口))]，手动去注册，放type即可
                    var serviceType = serviceAttribute.ServiceType;
                    //情况2 自动去找接口，如果存在就是接口，如果不存在就是本身
                    if (serviceType == null)
                    {
                        //获取最靠近的接口
                        var firstInter = type.GetInterfaces().LastOrDefault();
                        if (firstInter is null)
                        {
                            serviceType = type;
                        }
                        else
                        {
                            serviceType = firstInter;
                        }
                    }

                    switch (serviceAttribute.ServiceLifetime)
                    {
                        case LifeTime.Singleton:
                            if (type.IsGenericType)
                            {
                                build.RegisterGeneric(type).As(serviceType).SingleInstance().EnableInterfaceInterceptors();
                            }
                            else
                            {
                                build.RegisterType(type).As(serviceType).SingleInstance().EnableInterfaceInterceptors();
                            }
                            break;
                        case LifeTime.Scoped:
                            if (type.IsGenericType)
                            {
                                build.RegisterGeneric(type).As(serviceType).InstancePerLifetimeScope().EnableInterfaceInterceptors();
                            }
                            else
                            {
                                build.RegisterType(type).As(serviceType).InstancePerLifetimeScope().EnableInterfaceInterceptors();
                            }
                            break;
                        case LifeTime.Transient:
                            if (type.IsGenericType)
                            {
                                build.RegisterGeneric(type).As(serviceType).InstancePerDependency().EnableInterfaceInterceptors();
                            }
                            else
                            {
                                build.RegisterType(type).As(serviceType).InstancePerDependency().EnableInterfaceInterceptors();
                            }
                            break;
                        default:
                            if (type.IsGenericType)
                            {
                                build.RegisterGeneric(type).As(serviceType).InstancePerDependency().EnableInterfaceInterceptors();
                            }
                            else
                            {
                                build.RegisterType(type).As(serviceType).InstancePerDependency().EnableInterfaceInterceptors();
                            }
                            break;
                    }
                }
            }
        }

        public static void AddAutoIocService(this ContainerBuilder build, params string[] assemblyStr)
        {
            foreach (var a in assemblyStr)
            {
                RegIoc(build, Assembly.Load(a));
            }
        }
    }
}
