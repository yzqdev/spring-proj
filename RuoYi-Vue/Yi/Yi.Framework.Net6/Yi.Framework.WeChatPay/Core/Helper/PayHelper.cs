using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.WeChatPay.Options;

namespace Yi.Framework.WeChatPay.Core
{
    public class PayHelper
    {
        public static readonly string KEY_PAY_PREFIX = "order:pay:url:";
        private readonly IPayConfig _IPayConfig = null;
        private readonly PayApi _PayApi = null;

        private HttpContext _httpContext;
        public PayHelper(IPayConfig PayConfig, PayApi PayApi, IHttpContextAccessor httpContextAccessor)
        {
            this._IPayConfig = PayConfig;
            this._PayApi = PayApi;
            this._httpContext = httpContextAccessor.HttpContext;
        }

        /// <summary>
        /// 创建支付连接
        /// </summary>
        /// <param name="orderId"></param>
        /// <param name="description"></param>
        /// <param name="totalPay"></param>
        /// <param name="httpContext"></param>
        /// <returns></returns>
        public string CreatePayUrl(long orderId, string description, long totalPay)
        {
            // 定义返回的支付连接
            string url;
            try
            {
                // 构建支付需要的参数对象
                PayData data = new PayData();
                //描述
                data.SetValue("body", description);
                //订单号
                data.SetValue("out_trade_no", orderId.ToString());
                data.SetValue("product_id", orderId.ToString());
                //货币（默认就是人民币）
                data.SetValue("fee_type", "CNY");
                //TODO 总金额 （模拟1分钱， 线上环境换成真实价格）
                data.SetValue("total_fee", /*totalPay.ToString()*/ 1); // 单位是分
                //调用微信支付的终端ip
                data.SetValue("spbill_create_ip", "0.0.0.0");
                //回调地址
                data.SetValue("notify_url", this._IPayConfig.GetNotifyUrl());
                //交易类型为扫码支付
                data.SetValue("trade_type", "NATIVE");

                PayData result = this._PayApi.UnifiedOrder(data);//调用统一下单接口
                url = result.GetValue("code_url").ToString();//获得统一下单接口返回的二维码链接
            }
            catch (Exception e)
            {
                throw new Exception("生成支付链接连接失败", e);
            }

            return url;
        }


        /// <summary>
        /// （调用微信API）根据订单ID查询订单信息,全部信息
        /// </summary>
        /// <param name="transaction_id"></param>
        /// <returns></returns>
        public PayData QueryOrderById(long orderId)
        {
            PayData req = new PayData();
            req.SetValue("out_trade_no", orderId.ToString());
            PayData res = this._PayApi.OrderQuery(req, _httpContext);
            return res;// 返回查询数据
        }




        public static PayOptions GetPayOptions(string path)
        {
            string config = File.ReadAllText(path);
            var option = JsonConvert.DeserializeObject<PayOptions>(config);
            Console.WriteLine($"configPath={path} AppID={option.AppID}");
            return option;
        }

        /// <summary>
        /// 生成二维码方法
        /// </summary>
        /// <param name="text">输入的字符串</param>
        /// <param name="width">二维码宽度</param>
        /// <param name="height">二维码高度</param>
        /// <returns></returns>
        public static string QRcode(string text, int width = 360, int height = 360)
        {

            //这里要感谢一下http://old.wwei.cn/
            Dictionary<string, string> dic = new()
            {
                { "qrid", "0" },
                { "data[type]", "index" },
                { "data[text]", text },
                { "moban_id", "0" },
                { "size", "300" },
                { "level", "M" },
                { "moban_type", "qrcpu" },
                { "style_setting[protype]", "1" },
                { "style_setting[ptcolor]", "#000000" },
                { "style_setting[inptcolor]", "#000000" },
                { "style_setting[fcolor]", "#000000" },
                { "style_setting[bcolor]", "#ffffff" },
                { "style_setting[mbtype_hb]", "0" },
                { "style_setting[logo_id]", "" },
                { "style_setting[logo_width]", "46" },
                { "style_setting[logo_height]", "46" },
                { "style_setting[logo_border]", "0" }

            };


            StringBuilder builder = new StringBuilder();
            int i = 0;
            if (dic.Count > 0)
            {
                foreach (var item in dic)
                {
                    if (i > 0)
                        builder.Append("&");
                    builder.AppendFormat("{0}={1}", item.Key, item.Value);
                    i++;
                }
            }

            string postDataStr = builder.ToString();


#pragma warning disable SYSLIB0014 // 类型或成员已过时
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://old.wwei.cn/qrcode-wwei_create.html");
#pragma warning restore SYSLIB0014 // 类型或成员已过时

            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";
            request.ContentLength = Encoding.UTF8.GetByteCount(postDataStr);
            request.Headers.Add("Host", "old.wwei.cn");
            request.Headers.Add("User-Agent", "PostmanRuntime/6.66.6");
            request.Headers.Add("Origin", "http://old.wwei.cn");
            request.Headers.Add("Referer", "http://old.wwei.cn/");

            Stream myRequestStream = request.GetRequestStream();
            StreamWriter myStreamWriter = new StreamWriter(myRequestStream, Encoding.GetEncoding("utf-8"));
            myStreamWriter.Write(postDataStr);
            myStreamWriter.Close();

            HttpWebResponse response = (HttpWebResponse)request.GetResponse();

            Stream myResponseStream = response.GetResponseStream();
            StreamReader myStreamReader = new StreamReader(myResponseStream, Encoding.GetEncoding("utf-8"));
            string retString = myStreamReader.ReadToEnd();
            myStreamReader.Close();
            myResponseStream.Close();

            var json = Newtonsoft.Json.JsonConvert.DeserializeObject<JObject>(retString);
            var data = json["data"].ToString();

            return data;

        }
    }
}
