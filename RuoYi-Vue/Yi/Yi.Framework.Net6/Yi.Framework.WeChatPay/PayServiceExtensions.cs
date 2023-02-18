using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.WeChatPay.Core;
using Yi.Framework.WeChatPay.Exceptions;
using Yi.Framework.WeChatPay.Options;

namespace Yi.Framework.WeChatPay.WebCore
{
    public  static class PayServiceExtensions
    {
        public static IServiceCollection AddWeChatPayService(this IServiceCollection services,Action<PayOptions> configure)
        {
            var option= new PayOptions();
            configure(option);
            if (option.IsFileConfig)
            {
               option=PayHelper.GetPayOptions(option.ConfigPath);
            }
            if (option.AppID == null)
            {
                throw new PayException("AppId为空值");
            }
            if (option.MchID == null)
            {
                throw new PayException("MchID为空值");
            }
            if (option.Key == null)
            {
                throw new PayException("Key为空值");
            }
            if (option.NotifyUrl == null)
            {
                throw new PayException("NotifyUrl为空值");
            }
            services.AddSingleton(option);
            services.AddTransient<PayInvoker>();
            services.AddTransient<IPayInvoker, PayInvoker>();
            services.AddTransient<IPayConfig, PayConfig>();
            services.AddTransient<PayHelper>();
            services.AddTransient<PayApi>();
            services.AddTransient<PayHttpService>();
            services.AddTransient<AbstractNotify, ResultNotify>();
            services.AddHttpContextAccessor();
            return services;
          }
    }
}
