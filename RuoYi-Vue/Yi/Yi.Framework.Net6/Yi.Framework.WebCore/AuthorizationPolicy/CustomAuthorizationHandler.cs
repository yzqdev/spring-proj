using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.IdentityModel.JsonWebTokens;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;
using Yi.Framework.Core;

namespace Yi.Framework.WebCore.AuthorizationPolicy
{
    //策略验证的Handler  继承AuthorizationHandler 泛型类   泛型参数为  策略参数
    public class CustomAuthorizationHandler : AuthorizationHandler<CustomAuthorizationRequirement>
    {

        //private CacheClientDB _cacheClientDB;
        /// <summary>
        /// 构造函数
        /// </summary>
        public CustomAuthorizationHandler()
        {
        }

        //验证的方法就在这里
        protected override Task HandleRequirementAsync(AuthorizationHandlerContext context, CustomAuthorizationRequirement requirement)
        { 
            var currentClaim = context.User.Claims.FirstOrDefault(u => u.Type == JwtRegisteredClaimNames.Sid);
                //DefaultHttpContext httpcontext = (DefaultHttpContext)context.AuthenticateAsync();
            if (currentClaim!=null) //说明没有写入Sid  没有登录
            {
                context.Succeed(requirement); //验证通过了
            }
            //string currentUserId = "";
            //if (!string.IsNullOrWhiteSpace(currentClaim.Value))
            //{
            //    currentUserId = currentClaim.Value;
            //}
             //DefaultHttpContext httpcontext = (DefaultHttpContext)context.Resource;
            return Task.CompletedTask; //验证不同过
        }
    }


    /// <summary>
    /// 菜单权限策略
    /// </summary>
    public static class CustomAuthorizationHandlerExtension
    {
        public static Task AuthorizationMenueExtension(this AuthorizationHandlerContext handlerContext, CustomAuthorizationRequirement requirement)
        {
            bool bog = true;
            if (bog)
            {
                return Task.Run(() =>
                 {
                     handlerContext.Succeed(requirement); //验证通过了
                 });
            }
            else
            {
                return Task.CompletedTask; //验证不同过
            }
        }
    }
}
