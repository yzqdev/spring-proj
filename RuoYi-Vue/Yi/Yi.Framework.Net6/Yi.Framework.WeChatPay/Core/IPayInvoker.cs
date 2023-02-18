using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.WeChatPay.Core
{
    public interface IPayInvoker
    {
        /// <summary>
        /// 获取WX支付链接的方法
        /// 然后发布定时同步状态任务
        /// </summary>
        /// <param name="orderId">订单ID</param>
        /// <param name="user">用户信息</param>
        /// <param name="httpContext">请求上下文</param>
        /// <returns>返回生成的支持链接</returns>
        string GenerateUrl(long orderId, long totalPay);
        /// <summary>
        /// 处理微信支付回调
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        PayData HandleNotify();

    }
}
