using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Common.Enum
{
    public enum ResultCodeEnum
    {
        /// <summary>
        /// 操作成功。
        /// </summary>
        Success = 200,

        /// <summary>
        /// 操作不成功
        /// </summary>
        NotSuccess = 500,

        /// <summary>
        /// 无权限
        /// </summary>
        NoPermission = 401,

        /// <summary>
        ///  Access过期
        /// </summary>
        AccessTokenExpire = 1001,

        /// <summary>
        /// Refresh过期
        /// </summary>
        RefreshTokenExpire = 1002,

        /// <summary>
        /// 没有角色登录
        /// </summary>
        NoRoleLogin = 1003,
    }
}
