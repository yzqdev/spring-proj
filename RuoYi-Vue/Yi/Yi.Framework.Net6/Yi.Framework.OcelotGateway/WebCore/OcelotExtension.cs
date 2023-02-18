using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.Core.Cache;
using Yi.Framework.OcelotGateway.Builder;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.OcelotGateway.WebCore
{
    public class OcelotMiddleware
    {
        private readonly RequestDelegate next;
        private CacheInvoker _cacheClientDB;
        public OcelotMiddleware(RequestDelegate next, CacheInvoker cacheClientDB)
        {
            this.next = next;
            this._cacheClientDB = cacheClientDB;

        }
        public async Task Invoke(HttpContext context)
        {
            //--------------------------------------访问路径--------------------------------------------
            var path = context.Request.Path.Value!;
           var authorizationOptions= Appsettings.app<AuthorizationOptions>("AuthorizationOptions");
            //-------------------------------------刷新令牌路径-----------------------------------------------
            string refresh = authorizationOptions.Refresh;
            //-------------------------------------白名单------------------------------------------------
            List<string> whiteList = authorizationOptions.WhiteList;
            //------------------------------------白名单需鉴权------------------------------------------
            List<string> accountList = authorizationOptions.AccountList;
            //------------------------------------用户白名单------------------------------------------
            List<string> userList = authorizationOptions.UserList;
            //------------------------------------租户白名单------------------------------------------
            List<string> tenantList = authorizationOptions.TenantList;


            //--------------------------------------开始组装管道---------------------------------------------
            DataContext dataContext = new() {TenantPathList= tenantList,  Context = context,UserWhitePathList=userList, AccountPathList = accountList,  WhitePathList = whiteList, RefreshPath = refresh, Path = path, DB = _cacheClientDB };
            //--------------------------------------管道执行---------------------------------------------
            GateStartBuilder.Run(dataContext);
            //--------------------------------------处理结果---------------------------------------------
            if (dataContext.Result.Status)
            {
            //--------------------------------------中间件执行---------------------------------------------
                await next(context);
            }
            else
            {
                context.Response.ContentType = "application/json;charset=utf-8";
                await context.Response.WriteAsync(Common.Helper.JsonHelper.ObjToStr(dataContext.Result));
            }
        }

    }

    public static class OcelotExtensions
    {
        public static IApplicationBuilder UseOcelotExtensionService(this IApplicationBuilder builder)
        {
            return builder.UseMiddleware<OcelotMiddleware>();
        }
    }
}
