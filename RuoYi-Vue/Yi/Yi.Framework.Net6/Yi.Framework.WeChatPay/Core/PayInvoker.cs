using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.WeChatPay.Options;

namespace Yi.Framework.WeChatPay.Core
{
    public class PayInvoker:IPayInvoker
    {
        private readonly PayHelper _payHelper;
        private readonly ILogger<PayInvoker> _logger;
        private readonly AbstractNotify _notify;

        public PayInvoker(PayHelper payHelper, ILogger<PayInvoker> logger, AbstractNotify notify)
        {
            this._payHelper = payHelper;
            this._logger = logger;
            this._notify = notify;
        }

        /// <summary>
        /// 获取WX支付链接的方法
        /// 然后发布定时同步状态任务
        /// </summary>
        /// <param name="orderId">订单ID</param>
        /// <param name="user">用户信息</param>
        /// <param name="httpContext">请求上下文</param>
        /// <returns>返回生成的支持链接</returns>
        public string GenerateUrl(long orderId, long totalPay)
        {
            string payUrl = null;
            _logger.LogInformation("准备生成支付链接.....");
            payUrl = _payHelper.CreatePayUrl(orderId, "微信支付链接", totalPay);
            _logger.LogInformation("生成支付链接为:{payUrl}", payUrl);
            return payUrl;
        }

        /// <summary>
        /// 处理微信支付回调
        /// </summary>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public PayData HandleNotify()
        {
            PayData wxPayData = this._notify.ProcessNotify();

            return wxPayData;
        }

    }
}
