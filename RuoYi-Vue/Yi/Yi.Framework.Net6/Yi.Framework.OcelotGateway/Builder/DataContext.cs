using Microsoft.AspNetCore.Http;
using System.Collections.Generic;
using Yi.Framework.Common.Models;
using Yi.Framework.Core.Cache;

namespace Yi.Framework.OcelotGateway.Builder
{
    public class DataContext
    {
        //访问路径
        public string? Path { get; set; }

        //是否为用于刷新的token
        public bool? IsRe { get; set; } = false;

        //刷新令牌的路径
        public string? RefreshPath { get; set; }

        //用户白名单
        public List<string>? UserWhitePathList { get; set; }

        //白名单路径
        public List<string>? WhitePathList { get; set; }

        //直接放行但是需要鉴权
        public List<string>? AccountPathList { get; set; }

        /// <summary>
        /// 租户白名单
        /// </summary>
        public List<string>? TenantPathList { get; set; }

        //public UserRoleMenuEntity? UserRoleMenuEntity { get; set; }

        //最终的结果
        public Result Result { get; set; } = Result.UnAuthorize();

        public HttpContext? Context { get; set; }

        public CacheInvoker? DB { get; set; }

    }
}
