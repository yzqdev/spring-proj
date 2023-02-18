using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

[assembly: HostingStartup(typeof(Yi.Framework.WebCore.BuilderExtend.CustomHostingStartup))]
//这里放上启动类下的dll，即自动侵入了，当然我们现在不会这样做。
namespace Yi.Framework.WebCore.BuilderExtend
{
    /// <summary>
    /// 必须实现IHostingStartup接口
    /// 必须标记HostingStartup特性
    /// 
    /// 你小子！还是被你发现了啊，教你一招骚操作！这是一个屌的东西！
    /// 
    /// 我们称它为： 无侵注入！ 它彻头彻尾就是一个木马病毒！
    /// 
    /// </summary>
    public class CustomHostingStartup : IHostingStartup
    {
        public void Configure(IWebHostBuilder builder)
        {
            Console.WriteLine("This is CustomHostingStartup Invoke");

            //有IWebHostBuilder，一切都可以做。。
            #region MyRegion
            //builder.ConfigureAppConfiguration(configurationBuilder =>
            //{
            //    configurationBuilder.AddXmlFile("appsettings1.xml", optional: false, reloadOnChange: true);
            //});//添加配置

            //builder.ConfigureServices(services =>
            //{
            //    services.AddTransient<ITestServiceA, TestServiceA>();
            //});//IOC注册

            //builder.Configure(app =>
            //{
            //    app.Use(next =>
            //    {
            //        Console.WriteLine("This is CustomHostingStartup-Middleware  Init");
            //        return new RequestDelegate(
            //            async context =>
            //            {
            //                Console.WriteLine("This is CustomHostingStartup-Middleware start");
            //                await next.Invoke(context);
            //                Console.WriteLine("This is CustomHostingStartup-Middleware end");
            //            });
            //    });
            //});//甚至来个中间件
            #endregion
        }
    }
}
