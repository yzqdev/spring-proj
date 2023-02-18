global using System;
using Autofac.Extensions.DependencyInjection;
using Yi.Framework.WebCore.BuilderExtend;
using Yi.Framework.Core;
using Yi.Framework.WebCore.MiddlewareExtend;
using Autofac;
using Yi.Framework.Common.Models;
using Yi.Framework.Language;
using Microsoft.Extensions.Localization;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.SignalRHub;
using Hei.Captcha;
using Microsoft.Extensions.DependencyInjection;
using Yi.Framework.WebCore.DbExtend;
using IPTools.Core;
using Yi.Framework.WebCore.LogExtend;
using Microsoft.Extensions.DependencyInjection.Extensions;
using Microsoft.AspNetCore.Mvc.Controllers;
using Yi.Framework.WebCore.AutoFacExtend;
using AspectCore.Extensions.DependencyInjection;
using AspectCore.Extensions.Hosting;
using Yi.Framework.WebCore.AspNetCoreExtensions;
using Yi.Framework.WebCore.CommonExtend;

var builder = WebApplication.CreateBuilder(args);
builder.Configuration.AddCommandLine(args);
builder.WebHost.UseUrls(builder.Configuration.GetValue<string>("StartUrl"));
builder.Host.ConfigureAppConfiguration((hostBuilderContext, configurationBuilder) =>
 {
     configurationBuilder.AddCommandLine(args);
     configurationBuilder.AddJsonFileService();
     #region 
     //Apollo配置
     #endregion
     configurationBuilder.AddApolloService("Yi");
 });

builder.Host.UseServiceProviderFactory(new AutofacServiceProviderFactory());
builder.Host.ConfigureContainer<ContainerBuilder>(containerBuilder =>
{
    #region
    //交由Module依赖注入
    #endregion
    containerBuilder.RegisterModule<CustomAutofacModule>();
    #region
    //添加属性注入模块
    #endregion
    containerBuilder.RegisterModule<PropertiesAutowiredModule>();
    #region
    //使用AppService特性优雅的进行自动依赖注入,仓储与基类服务便是使用该种方式自动注入
    #endregion
    containerBuilder.AddAutoIocService("Yi.Framework.Repository", "Yi.Framework.Service");
});

////属性注入，将控制器的mvc模块转接给ioc
builder.Services.Replace(ServiceDescriptor.Transient<IControllerActivator, ServiceBasedControllerActivator>());

builder.Host.ConfigureLogging(loggingBuilder =>
                {
                    loggingBuilder.AddFilter("System", Microsoft.Extensions.Logging.LogLevel.Warning);
                    loggingBuilder.AddFilter("Microsoft", Microsoft.Extensions.Logging.LogLevel.Warning);
                    loggingBuilder.AddLog4Net("./Config/Log4net.config");
                    //添加自定义日志
                    //loggingBuilder.AddCustomLogger();
                });
#region
//配置类配置
//builder.Host.ConfigureWebHostDefaults(webBuilder =>
//                {
//                    //webBuilder.UseStartup<Startup>();
//                });
#endregion
//-----------------------------------------------------------------------------------------------------------
#region
//Ioc配置
#endregion
builder.Services.AddIocService(builder.Configuration);
#region
//Sqlsugar上下文注入,是否开启数据权限功能，开启需走缓存
#endregion
builder.Services.AddSqlsugarServer(DbFiterExtend.Data);
#region
//Quartz任务调度配置
#endregion
builder.Services.AddQuartzService();
#region
//AutoMapper注入
#endregion
builder.Services.AddAutoMapperService();
#region
//控制器+过滤器配置
#endregion
builder.Services.AddControllers(optios =>
{
    //注册全局
    optios.Filters.Add<GlobalLogAttribute>();
}).AddJsonFileService();
#region
//权限过滤器
#endregion
//权限
builder.Services.AddSingleton<PermissionAttribute>();
//日志
builder.Services.AddSingleton<GlobalLogAttribute>();
#region
//Swagger服务配置
#endregion
builder.Services.AddSwaggerService<Program>();
#region
//跨域服务配置
#endregion
builder.Services.AddCorsService();
#region
//Jwt鉴权配置
#endregion
builder.Services.AddJwtService();
#region
//授权配置
#endregion
builder.Services.AddAuthorizationService();
#region
//Redis服务配置
#endregion
builder.Services.AddCacheService();
#region
//RabbitMQ服务配置
#endregion
builder.Services.AddRabbitMQService();
#region
//ElasticSeach服务配置
#endregion
builder.Services.AddElasticSeachService();
#region
//短信服务配置
#endregion
builder.Services.AddSMSService();
#region
//CAP服务配置
#endregion
builder.Services.AddCAPService();
#region
//国际化配置
#endregion
builder.Services.AddLocalizerService();
#region
//添加signalR
#endregion
builder.Services.AddSignalR();
#region
//添加验证码
#endregion
builder.Services.AddHeiCaptcha();
#region
//添加Http上下文
#endregion
builder.Services.AddHttpContextAccessor();
#region
//添加缩略图，引入了System.Drawing，linu需要插件支持
#endregion
builder.Services.AddSingleton<ThumbnailSharpInvoer>();

#region
//添加当前用户信息使用
#endregion
builder.Services.AddCurrentUserServer();

#region
//全局配置初始化值
#endregion
GobalModel.SqlLogEnable = Appsettings.appBool("SqlLog_Enable");
GobalModel.LoginCodeEnable = Appsettings.appBool("LoginCode_Enable");
//-----------------------------------------------------------------------------------------------------------
var app = builder.Build();
#region
//服务容器
#endregion
ServiceLocator.Instance = app.Services;
//if (app.Environment.IsDevelopment())
{
    #region
    //测试页面注入
    #endregion
    app.UseDeveloperExceptionPage();
    app.UseSwaggerService();
}
#region
//错误抓取反馈注入
#endregion
app.UseErrorHandlingService();

#region
//静态文件注入
#endregion
app.UseStaticFiles();
#region
//多语言国际化注入
#endregion
app.UseLocalizerService();

#region
//上下文请求body
#endregion
app.UseHttpBodyService();
#region
//HttpsRedirection注入
#endregion
app.UseHttpsRedirection();
#region
//路由注入
#endregion
app.UseRouting();
#region
//跨域服务注入
#endregion
app.UseCorsService();
#region
//健康检查注入
#endregion
app.UseHealthCheckService();
#region
//鉴权注入
#endregion
app.UseAuthentication();
#region
//授权注入
#endregion
app.UseAuthorization();
#region
//添加当前用户信息使用
#endregion
app.UseCurrentUserServer();
#region
//Consul服务注入
#endregion
app.UseConsulService();

#region
//数据库种子注入
#endregion
app.UseDbSeedInitService();
#region
//redis种子注入
#endregion
app.UseRedisSeedInitService();

app.UseEndpoints(endpoints =>
{
    #region
    //SignalR配置
    #endregion
    endpoints.MapHub<MainHub>("/api/hub/main");
    endpoints.MapControllers();
});
//准备添加多租户
app.Run();