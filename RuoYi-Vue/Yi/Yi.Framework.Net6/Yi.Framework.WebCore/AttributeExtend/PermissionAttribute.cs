using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.IdentityModel.JsonWebTokens;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;

namespace Yi.Framework.WebCore.AttributeExtend
{
    [AttributeUsage(AttributeTargets.Method)]
    public class PermissionAttribute : ActionFilterAttribute
    {
        private string permission { get; set; }

        public PermissionAttribute(string permission)
        {
            this.permission = permission;
        }

        /// <summary>
        /// 动作鉴权
        /// </summary>
        /// <param name="context"></param>
        /// <exception cref="Exception"></exception>
        public override void OnActionExecuting(ActionExecutingContext context)
        {
            if (string.IsNullOrEmpty(permission))
            {
                throw new Exception("权限不能为空！");
            }

            var result = false;


            //可以从Redis得到用户菜单列表，或者直接从jwt中获取
            var sid = context.HttpContext.User.Claims.FirstOrDefault(u => u.Type == JwtRegisteredClaimNames.Sid);

            //jwt存在的权限列表
            var perList = context.HttpContext.User.Claims.Where(u => u.Type == SystemConst.PermissionClaim).Select(u => u.Value.ToString().ToLower()).ToList();
            //判断权限是否存在Redis中,或者jwt中

            //进行正则表达式的匹配，以code开头
            Regex regex = new Regex($"^{permission.ToLower()}");
            foreach (var p in perList)
            {
                //如果存在超级管理员权限，直接放行
                if (SystemConst.AdminPermissionCode.Equals(p))
                {
                    result = true;
                    break;
                }

                if (regex.IsMatch(p))
                {
                    result = true;
                    break;
                }
            }
            //用户的增删改查直接可以user:*即可

            if (!result)
            {
                throw new Exception("拦截未授权请求！");
            }
        }

    }
}