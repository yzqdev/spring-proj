using FizzWare.NBuilder;
using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.WeChatPay.Exceptions;

namespace Yi.Framework.WeChatPay.Core
{
    public class PayApi
    {
        private readonly IPayConfig _IPayConfig = null;
        private readonly PayHttpService _PayHttpService = null;
        public PayApi(IPayConfig payConfig, PayHttpService payHttpService)
        {
            this._IPayConfig = payConfig;
            this._PayHttpService = payHttpService;
        }

        /**
        * 
        * 统一下单
        * @param WxPaydata inputObj 提交给统一下单API的参数
        * @param int timeOut 超时时间
        * @throws WxPayException
        * @return 成功时返回，其他抛异常
        */
        public PayData UnifiedOrder(PayData inputObj, int timeOut = 6)
        {
            string url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            //检测必填参数
            if (!inputObj.IsSet("out_trade_no"))
            {
                throw new PayException("缺少统一支付接口必填参数out_trade_no！");
            }
            else if (!inputObj.IsSet("body"))
            {
                throw new PayException("缺少统一支付接口必填参数body！");
            }
            else if (!inputObj.IsSet("total_fee"))
            {
                throw new PayException("缺少统一支付接口必填参数total_fee！");
            }
            else if (!inputObj.IsSet("trade_type"))
            {
                throw new PayException("缺少统一支付接口必填参数trade_type！");
            }

            //关联参数
            if (inputObj.GetValue("trade_type").ToString() == "JSAPI" && !inputObj.IsSet("openid"))
            {
                throw new PayException("统一支付接口中，缺少必填参数openid！trade_type为JSAPI时，openid为必填参数！");
            }
            if (inputObj.GetValue("trade_type").ToString() == "NATIVE" && !inputObj.IsSet("product_id"))
            {
                throw new PayException("统一支付接口中，缺少必填参数product_id！trade_type为JSAPI时，product_id为必填参数！");
            }

            //异步通知url未设置，则使用配置文件中的url
            /*if (!inputObj.IsSet("notify_url"))
            {
                inputObj.SetValue("notify_url", this._IWxPayConfig().GetNotifyUrl());//异步通知url
            }*/

            inputObj.SetValue("appid", this._IPayConfig.GetAppID());//公众账号ID
            inputObj.SetValue("mch_id", this._IPayConfig.GetMchID());//商户号
            inputObj.SetValue("spbill_create_ip", this._IPayConfig.GetIp());//终端ip	  	    
            inputObj.SetValue("nonce_str", GenerateNonceStr());//随机字符串
            inputObj.SetValue("sign_type", PayData.SIGN_TYPE_HMAC_SHA256);//签名类型

            //签名
            inputObj.SetValue("sign", inputObj.MakeSign());
            string xml = inputObj.ToXml();
            // 发起http请求
            string response = this._PayHttpService.Post(xml, url, false, timeOut);
            PayData result = new PayData();
            result.FromXml(response);

            return result;
        }

        /**
        *    
        * 查询订单
        * @param WxPayData inputObj 提交给查询订单API的参数
        * @param int timeOut 超时时间
        * @throws WxPayException
        * @return 成功时返回订单查询结果，其他抛异常
        */
        public PayData OrderQuery(PayData inputObj, HttpContext httpContext, int timeOut = 6)
        {
            string url = "https://api.mch.weixin.qq.com/pay/orderquery";
            //检测必填参数
            if (!inputObj.IsSet("out_trade_no") && !inputObj.IsSet("transaction_id"))
            {
                throw new PayException("订单查询接口中，out_trade_no、transaction_id至少填一个！");
            }

            inputObj.SetValue("appid", this._IPayConfig.GetAppID());//公众账号ID
            inputObj.SetValue("mch_id", this._IPayConfig.GetMchID());//商户号
            inputObj.SetValue("nonce_str", GenerateNonceStr());//随机字符串
            inputObj.SetValue("sign_type", PayData.SIGN_TYPE_HMAC_SHA256);//签名类型
            inputObj.SetValue("sign", inputObj.MakeSign());//签名
            string xml = inputObj.ToXml();
            //Log.Debug("WxPayApi", "OrderQuery request : " + xml);
            string response = this._PayHttpService.Post(xml, url, false, timeOut);//调用HTTP通信接口提交数据
            //Log.Debug("WxPayApi", "OrderQuery response : " + response);

            //将xml格式的数据转化为对象以返回
            PayData result = new PayData();
            result.FromXml(response);

            return result;
        }

        /**
        * 生成时间戳，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数
         * @return 时间戳
        */
        public static string GenerateTimeStamp()
        {
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
            return Convert.ToInt64(ts.TotalSeconds).ToString();
        }

        /**
        * 生成随机串，随机串包含字母或数字
        * @return 随机串
        */
        public static string GenerateNonceStr()
        {
            RandomGenerator randomGenerator = new RandomGenerator();
            return randomGenerator.Int().ToString();
        }
    }
}
