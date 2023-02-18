using Autofac;
using Autofac.Core;
using Autofac.Extras.DynamicProxy;
using Castle.DynamicProxy;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ApplicationParts;
using Microsoft.AspNetCore.Mvc.Controllers;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Interface;
using Yi.Framework.Interface.Base.Crud;
using Yi.Framework.Job;
using Yi.Framework.Repository;
using Yi.Framework.Service;
using Yi.Framework.Service.Base.Crud;
using Module = Autofac.Module;

namespace Yi.Framework.WebCore.AutoFacExtend
{
    public class PropertiesAutowiredModule : Module
    {
   
        protected override void Load(ContainerBuilder containerBuilder)
        {
            //这里设置哪些程序集可以进行属性注入，默认控制器即可

            // 获取所有控制器类型并使用属性注入
            var controllerBaseType = typeof(ControllerBase);
            //先暂时获取这里的程序集，之后改造通过option进行配置
            containerBuilder.RegisterAssemblyTypes( Assembly.Load("Yi.Framework.ApiMicroservice"))
                .Where(t => controllerBaseType.IsAssignableFrom(t) && t != controllerBaseType)
                .PropertiesAutowired(new AutowiredPropertySelector());

        }

    }

    public class AutowiredPropertySelector : IPropertySelector
    {
        public bool InjectProperty(PropertyInfo propertyInfo, object instance)
        {
            return propertyInfo.CustomAttributes.Any(it => it.AttributeType == typeof(AutowiredAttribute));
        }
    }

   
}