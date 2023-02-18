using Yi.Framework.Model;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using System.IdentityModel.Tokens.Jwt;
using System.IO;
using System.Text.RegularExpressions;
using UAParser;
using IPTools.Core;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.WebCore.CommonExtend
{
    public static class HttpContextExtend
    {
        /// <summary>
        /// 判断是否为异步请求
        /// </summary>
        /// <param name="request"></param>
        /// <returns></returns>
        public static bool IsAjaxRequest(this HttpRequest request)
        {
            string header = request.Headers["X-Requested-With"];
            return "XMLHttpRequest".Equals(header);
        }

        /// <summary>
        /// 通过鉴权完的token获取用户id
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public static long GetUserIdInfo(this HttpContext httpContext)
        {
            var p = httpContext;
            var value = httpContext.User.Claims.FirstOrDefault(u => u.Type == JwtRegisteredClaimNames.Sid)?.Value;
            if (value is not null)
            {
                return Convert.ToInt64(value);
            }
            return 0;
        }

        /// <summary>
        /// 通过鉴权完的token获取用户名
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public static string? GetUserNameInfo(this HttpContext httpContext)
        {
            var p = httpContext;
            return httpContext.User.Claims.FirstOrDefault(u => u.Type == "userName")?.Value;
        }

        /// <summary>
        /// 通过鉴权完的token获取用户部门
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public static string? GetDeptIdInfo(this HttpContext httpContext)
        {
            var p = httpContext;
            return httpContext.User.Claims.FirstOrDefault(u => u.Type == "deptId")?.Value;
        }

        /// <summary>
        /// 通过鉴权完的token获取权限code
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public static string? GetPermissionInfo(this HttpContext httpContext)
        {
            var p = httpContext;
            return httpContext.User.Claims.FirstOrDefault(u => u.Type == "permission")?.Value;
        }


        /// <summary>
        /// 基于HttpContext,当前鉴权方式解析，获取用户信息
        /// 现在使用redis作为缓存，不需要将菜单存放至jwt中了
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public static UserEntity GetUserEntityInfo(this HttpContext httpContext, out List<Guid> menuIds)
        {
            IEnumerable<Claim>? claimlist = null;
            long resId = 0;
            try
            {
                claimlist = httpContext.User.Claims;
                resId = Convert.ToInt64(claimlist.FirstOrDefault(u => u.Type == JwtRegisteredClaimNames.Sid)?.Value);
            }
            catch
            {
                throw new Exception("未授权，Token鉴权失败！");
            }
            menuIds = claimlist.Where(u => u.Type == "menuIds").ToList().Select(u => new Guid(u.Value)).ToList();
            return new UserEntity()
            {
                Id = resId,
                //Name = claimlist.FirstOrDefault(u => u.Type == JwtRegisteredClaimNames.Name).Value
            };
        }

        /// <summary>
        /// 设置文件下载名称
        /// </summary>
        /// <param name="httpContext"></param>
        /// <param name="fileName"></param>
        public static void FileInlineHandle(this HttpContext httpContext, string fileName)
        {
            string encodeFilename = System.Web.HttpUtility.UrlEncode(fileName, Encoding.GetEncoding("UTF-8"));
            httpContext.Response.Headers.Add("Content-Disposition", "inline;filename=" + encodeFilename);

        }

        /// <summary>
        /// 设置文件附件名称
        /// </summary>
        /// <param name="httpContext"></param>
        /// <param name="fileName"></param>
        public static void FileAttachmentHandle(this HttpContext httpContext, string fileName)
        {
            string encodeFilename = System.Web.HttpUtility.UrlEncode(fileName, Encoding.GetEncoding("UTF-8"));
            httpContext.Response.Headers.Add("Content-Disposition", "attachment;filename=" + encodeFilename);

        }

        /// <summary>
        /// 获取语言种类
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public static string GetLanguage(this HttpContext httpContext)
        {
            string res = "zh-CN";
            var str = httpContext.Request.Headers["Accept-Language"].FirstOrDefault();
            if (str is not null)
            {
                res = str.Split(",")[0];
            }
            return res;

        }


        /// <summary>
        /// 获取请求Body参数
        /// </summary>
        /// <param name="context"></param>
        /// <param name="reqMethod"></param>
        /// <returns></returns>
        public static string GetRequestValue(this HttpContext context, string reqMethod)
        {
            string param;

            if (HttpMethods.IsPost(reqMethod) || HttpMethods.IsPut(reqMethod))
            {
                context.Request.Body.Seek(0, SeekOrigin.Begin);
                using var reader = new StreamReader(context.Request.Body, Encoding.UTF8);
                //需要使用异步方式才能获取
                param = reader.ReadToEndAsync().Result;
            }
            else
            {
                param = context.Request.QueryString.Value is null ? "" : context.Request.QueryString.Value.ToString();
            }
            return param;
        }

        /// <summary>
        /// 获取客户端信息
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public static ClientInfo GetClientInfo(this HttpContext context)
        {
            var str = context.GetUserAgent();
            var uaParser = Parser.GetDefault();
            ClientInfo c = uaParser.Parse(str);
            return c;
        }


        /// <summary>
        /// 获取客户端IP
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public static string GetClientIp(this HttpContext context)
        {
            if (context == null) return "";
            var result = context.Request.Headers["X-Forwarded-For"].FirstOrDefault();
            if (string.IsNullOrEmpty(result))
            {
                result = context.Connection.RemoteIpAddress?.ToString();
            }
            if (string.IsNullOrEmpty(result) || result.Contains("::1"))
                result = "127.0.0.1";

            result = result.Replace("::ffff:", "127.0.0.1");

            //Ip规则效验
            var regResult = Regex.IsMatch(result, @"^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$");

            result = regResult ? result : "127.0.0.1";
            return result;
        }

        /// <summary>
        /// 获取浏览器标识
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public static string GetUserAgent(this HttpContext context)
        {
            return context.Request.Headers["User-Agent"];
        }


        /// <summary>
        /// 记录用户登陆信息
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public static LoginLogEntity GetLoginLogInfo(this HttpContext context)
        {
            var ipAddr = context.GetClientIp();
            IpInfo location;
            if (ipAddr == "127.0.0.1")
            {
                location = new IpInfo() { Province = "本地", City = "本机" };
            }
            else
            {
                location = IpTool.Search(ipAddr);
            }
            ClientInfo clientInfo = context.GetClientInfo();
            LoginLogEntity entity = new()
            {
                Browser = clientInfo.Device.Family,
                Os = clientInfo.OS.ToString(),
                LoginIp = ipAddr,
                //登录是没有token的，所有是获取不到用户名，需要在控制器赋值
                //LoginUser = context.GetUserNameInfo(),
                LoginLocation = location.Province + "-" + location.City,
                IsDeleted = false
            };

            return entity;
        }

    }
}
