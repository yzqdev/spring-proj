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
     //Apollo����
     #endregion
     configurationBuilder.AddApolloService("Yi");
 });

builder.Host.UseServiceProviderFactory(new AutofacServiceProviderFactory());
builder.Host.ConfigureContainer<ContainerBuilder>(containerBuilder =>
{
    #region
    //����Module����ע��
    #endregion
    containerBuilder.RegisterModule<CustomAutofacModule>();
    #region
    //�������ע��ģ��
    #endregion
    containerBuilder.RegisterModule<PropertiesAutowiredModule>();
    #region
    //ʹ��AppService�������ŵĽ����Զ�����ע��,�ִ������������ʹ�ø��ַ�ʽ�Զ�ע��
    #endregion
    containerBuilder.AddAutoIocService("Yi.Framework.Repository", "Yi.Framework.Service");
});

////����ע�룬����������mvcģ��ת�Ӹ�ioc
builder.Services.Replace(ServiceDescriptor.Transient<IControllerActivator, ServiceBasedControllerActivator>());

builder.Host.ConfigureLogging(loggingBuilder =>
                {
                    loggingBuilder.AddFilter("System", Microsoft.Extensions.Logging.LogLevel.Warning);
                    loggingBuilder.AddFilter("Microsoft", Microsoft.Extensions.Logging.LogLevel.Warning);
                    loggingBuilder.AddLog4Net("./Config/Log4net.config");
                    //����Զ�����־
                    //loggingBuilder.AddCustomLogger();
                });
#region
//����������
//builder.Host.ConfigureWebHostDefaults(webBuilder =>
//                {
//                    //webBuilder.UseStartup<Startup>();
//                });
#endregion
//-----------------------------------------------------------------------------------------------------------
#region
//Ioc����
#endregion
builder.Services.AddIocService(builder.Configuration);
#region
//Sqlsugar������ע��,�Ƿ�������Ȩ�޹��ܣ��������߻���
#endregion
builder.Services.AddSqlsugarServer(DbFiterExtend.Data);
#region
//Quartz�����������
#endregion
builder.Services.AddQuartzService();
#region
//AutoMapperע��
#endregion
builder.Services.AddAutoMapperService();
#region
//������+����������
#endregion
builder.Services.AddControllers(optios =>
{
    //ע��ȫ��
    optios.Filters.Add<GlobalLogAttribute>();
}).AddJsonFileService();
#region
//Ȩ�޹�����
#endregion
//Ȩ��
builder.Services.AddSingleton<PermissionAttribute>();
//��־
builder.Services.AddSingleton<GlobalLogAttribute>();
#region
//Swagger��������
#endregion
builder.Services.AddSwaggerService<Program>();
#region
//�����������
#endregion
builder.Services.AddCorsService();
#region
//Jwt��Ȩ����
#endregion
builder.Services.AddJwtService();
#region
//��Ȩ����
#endregion
builder.Services.AddAuthorizationService();
#region
//Redis��������
#endregion
builder.Services.AddCacheService();
#region
//RabbitMQ��������
#endregion
builder.Services.AddRabbitMQService();
#region
//ElasticSeach��������
#endregion
builder.Services.AddElasticSeachService();
#region
//���ŷ�������
#endregion
builder.Services.AddSMSService();
#region
//CAP��������
#endregion
builder.Services.AddCAPService();
#region
//���ʻ�����
#endregion
builder.Services.AddLocalizerService();
#region
//���signalR
#endregion
builder.Services.AddSignalR();
#region
//�����֤��
#endregion
builder.Services.AddHeiCaptcha();
#region
//���Http������
#endregion
builder.Services.AddHttpContextAccessor();
#region
//�������ͼ��������System.Drawing��linu��Ҫ���֧��
#endregion
builder.Services.AddSingleton<ThumbnailSharpInvoer>();

#region
//��ӵ�ǰ�û���Ϣʹ��
#endregion
builder.Services.AddCurrentUserServer();

#region
//ȫ�����ó�ʼ��ֵ
#endregion
GobalModel.SqlLogEnable = Appsettings.appBool("SqlLog_Enable");
GobalModel.LoginCodeEnable = Appsettings.appBool("LoginCode_Enable");
//-----------------------------------------------------------------------------------------------------------
var app = builder.Build();
#region
//��������
#endregion
ServiceLocator.Instance = app.Services;
//if (app.Environment.IsDevelopment())
{
    #region
    //����ҳ��ע��
    #endregion
    app.UseDeveloperExceptionPage();
    app.UseSwaggerService();
}
#region
//����ץȡ����ע��
#endregion
app.UseErrorHandlingService();

#region
//��̬�ļ�ע��
#endregion
app.UseStaticFiles();
#region
//�����Թ��ʻ�ע��
#endregion
app.UseLocalizerService();

#region
//����������body
#endregion
app.UseHttpBodyService();
#region
//HttpsRedirectionע��
#endregion
app.UseHttpsRedirection();
#region
//·��ע��
#endregion
app.UseRouting();
#region
//�������ע��
#endregion
app.UseCorsService();
#region
//�������ע��
#endregion
app.UseHealthCheckService();
#region
//��Ȩע��
#endregion
app.UseAuthentication();
#region
//��Ȩע��
#endregion
app.UseAuthorization();
#region
//��ӵ�ǰ�û���Ϣʹ��
#endregion
app.UseCurrentUserServer();
#region
//Consul����ע��
#endregion
app.UseConsulService();

#region
//���ݿ�����ע��
#endregion
app.UseDbSeedInitService();
#region
//redis����ע��
#endregion
app.UseRedisSeedInitService();

app.UseEndpoints(endpoints =>
{
    #region
    //SignalR����
    #endregion
    endpoints.MapHub<MainHub>("/api/hub/main");
    endpoints.MapControllers();
});
//׼����Ӷ��⻧
app.Run();