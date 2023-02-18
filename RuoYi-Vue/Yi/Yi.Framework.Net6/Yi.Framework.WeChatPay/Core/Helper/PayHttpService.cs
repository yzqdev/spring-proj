using System;
using System.Collections.Generic;
using System.Web;
using System.Net;
using System.IO;
using System.Text;
using System.Net.Security;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using Microsoft.AspNetCore.Http;
using Yi.Framework.WeChatPay.Exceptions;

namespace Yi.Framework.WeChatPay.Core
{
    /// <summary>
    /// http连接基础类，负责底层的http通信
    /// </summary>
    public class PayHttpService
    {
        private readonly IPayConfig _IPayConfig = null;
        private readonly HttpContext _httpContext = null;
        public PayHttpService(IPayConfig PayConfig, IHttpContextAccessor httpContextAccessor)
        {
            this._IPayConfig = PayConfig;
            this._httpContext = httpContextAccessor.HttpContext;
        }

        public static bool CheckValidationResult(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
        {
            //直接确认，否则打不开    
            return true;
        }

        public string Post(string xml, string url, bool isUseCert, int timeout)
        {
            System.GC.Collect();//垃圾回收，回收没有正常关闭的http连接

            string result = "";//返回结果


            HttpWebRequest request = null;
            HttpWebResponse response = null;
            Stream reqStream = null;

            try
            {
                //设置最大连接数
                ServicePointManager.DefaultConnectionLimit = 200;
                //设置https验证方式
                if (url.StartsWith("https", StringComparison.OrdinalIgnoreCase))
                {
                    ServicePointManager.ServerCertificateValidationCallback =
                            new RemoteCertificateValidationCallback(CheckValidationResult);
                }

                /***************************************************************
                * 下面设置HttpWebRequest的相关属性
                * ************************************************************/
#pragma warning disable SYSLIB0014 // 类型或成员已过时
                request = (HttpWebRequest)WebRequest.Create(url);
#pragma warning restore SYSLIB0014 // 类型或成员已过时
                request.UserAgent = string.Format("WXPaySDK/{3} ({0}) .net/{1} {2}", Environment.OSVersion, Environment.Version, this._IPayConfig.GetMchID(), typeof(PayHttpService).Assembly.GetName().Version);
                request.Method = "POST";
                request.Timeout = timeout * 1000;

                //设置POST的数据类型和长度
                request.ContentType = "text/xml";
                byte[] data = Encoding.UTF8.GetBytes(xml);
                request.ContentLength = data.Length;


                if (isUseCert)//是否使用证书--没用证书
                {
                    string path = _httpContext.Request.Path;
                    X509Certificate2 cert = new X509Certificate2(path + this._IPayConfig.GetSSlCertPath(), this._IPayConfig.GetSSlCertPassword());
                    request.ClientCertificates.Add(cert);
                }

                //往服务器写入数据
                reqStream = request.GetRequestStream();
                reqStream.Write(data, 0, data.Length);
                reqStream.Close();

                //获取服务端返回
                response = (HttpWebResponse)request.GetResponse();

                //获取服务端返回数据
                StreamReader sr = new StreamReader(response.GetResponseStream(), Encoding.UTF8);
                result = sr.ReadToEnd().Trim();
                sr.Close();
            }
            catch (System.Threading.ThreadAbortException)
            {
#pragma warning disable SYSLIB0006 // 类型或成员已过时
                Thread.ResetAbort();
#pragma warning restore SYSLIB0006 // 类型或成员已过时
            }
            catch (WebException e)
            {
                if (e.Status == WebExceptionStatus.ProtocolError)
                {
                    throw new PayException(e.ToString());
                }

            }
            catch (Exception e)
            {
                throw new PayException(e.ToString());
            }
            finally
            {
                //关闭连接和流
                if (response != null)
                {
                    response.Close();
                }
                if (request != null)
                {
                    request.Abort();
                }
            }
            return result;
        }

        /// <summary>
        /// 处理http GET请求，返回数据
        /// </summary>
        /// <param name="url">请求的url地址</param>
        /// <returns>http GET成功后返回的数据，失败抛WebException异常</returns>
        public string Get(string url)
        {
            System.GC.Collect();
            string result = "";

            HttpWebRequest request = null;
            HttpWebResponse response = null;

            //请求url以获取数据
            try
            {
                //设置最大连接数
                ServicePointManager.DefaultConnectionLimit = 200;
                //设置https验证方式
                if (url.StartsWith("https", StringComparison.OrdinalIgnoreCase))
                {
                    ServicePointManager.ServerCertificateValidationCallback =
                            new RemoteCertificateValidationCallback(CheckValidationResult);
                }

                /***************************************************************
                * 下面设置HttpWebRequest的相关属性
                * ************************************************************/
#pragma warning disable SYSLIB0014 // 类型或成员已过时
                request = (HttpWebRequest)WebRequest.Create(url);
#pragma warning restore SYSLIB0014 // 类型或成员已过时
                request.UserAgent = string.Format("WXPaySDK/{3} ({0}) .net/{1} {2}", Environment.OSVersion, Environment.Version, this._IPayConfig.GetMchID(), typeof(PayHttpService).Assembly.GetName().Version);
                request.Method = "GET";

                //获取服务器返回
                response = (HttpWebResponse)request.GetResponse();

                //获取HTTP返回数据
                StreamReader sr = new StreamReader(response.GetResponseStream(), Encoding.UTF8);
                result = sr.ReadToEnd().Trim();
                sr.Close();
            }
            catch (System.Threading.ThreadAbortException)
            {
#pragma warning disable SYSLIB0006 // 类型或成员已过时
                Thread.ResetAbort();
#pragma warning restore SYSLIB0006 // 类型或成员已过时
            }
            catch (WebException e)
            {
                throw new PayException(e.ToString());
            }
            catch (Exception e)
            {
                throw new PayException(e.ToString());
            }
            finally
            {
                //关闭连接和流
                if (response != null)
                {
                    response.Close();
                }
                if (request != null)
                {
                    request.Abort();
                }
            }
            return result;
        }
    }
}