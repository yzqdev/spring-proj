using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.WeChatPay.Exceptions;

namespace Yi.Framework.WeChatPay.Core
{
    public abstract class AbstractNotify
    {
        protected HttpContext _httpContext;

        public AbstractNotify(IHttpContextAccessor httpContextAccessor)
        {
            this._httpContext = httpContextAccessor.HttpContext;
        }

        /// <summary>
        /// 从请求对象中获取信息
        /// </summary>
        /// <param name="request"></param>
        /// <param name="encoding"></param>
        /// <returns></returns>
        private async Task<string> GetRawBodyStringAsync(HttpRequest request, Encoding encoding = null)
        {
            if (encoding is null)
            {
                encoding = Encoding.UTF8;
            }
            var stream = new MemoryStream();
            await request.Body.CopyToAsync(stream);
            stream.Seek(0, 0);
            using (var reader = new StreamReader(stream, encoding))
            {
                var result = await reader.ReadToEndAsync();
                return result;
            }
        }

        /// <summary>
        /// 接收从微信支付后台发送过来的数据并验证签名
        /// </summary>
        /// <returns>微信支付后台返回的数据</returns>
        public PayData GetNotifyData()
        {
            //接收从微信后台POST过来的数据
            string content = GetRawBodyStringAsync(_httpContext.Request).Result;
            Console.WriteLine(this.GetType().ToString(), "Receive data from WeChat : " + content);

            //转换数据格式并验证签名
            PayData data = new PayData();
            try
            {
                data.FromXml(content);
            }
            catch (PayException ex)
            {
                throw new Exception("验签失败", ex);
            }

            Console.WriteLine(this.GetType().ToString(), "Check sign success");
            return data;
        }

        //派生类需要重写这个方法，进行不同的回调处理
        public abstract PayData ProcessNotify();

    }
}
