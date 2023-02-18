using IPTools.Core;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Controllers;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AttributeExtend
{
    public class GlobalLogAttribute : ActionFilterAttribute
    {
        private ILogger<GlobalLogAttribute> _logger;
        private IOperationLogService _operationLogService;
        //注入一个日志服务
        public GlobalLogAttribute(ILogger<GlobalLogAttribute> logger, IOperationLogService operationLogService)
        {
            _logger = logger;
            _operationLogService = operationLogService;
        }

        public override void OnResultExecuted(ResultExecutedContext context)
        {
            try
            {
                //判断标签是在方法上
                if (context.ActionDescriptor is not ControllerActionDescriptor controllerActionDescriptor) return;

                //查找标签，获取标签对象
                LogAttribute? logAttribute = controllerActionDescriptor.MethodInfo.GetCustomAttributes(inherit: true)
                  .FirstOrDefault(a => a.GetType().Equals(typeof(LogAttribute))) as LogAttribute;
                //空对象直接返回
                if (logAttribute is null) return;

                ////获取控制器名
                //string controller = context.RouteData.Values["Controller"].ToString();

                ////获取方法名
                //string action = context.RouteData.Values["Action"].ToString();

                //获取Ip
                string ip = context.HttpContext.GetClientIp();

                //根据ip获取地址

                var ipTool = IpTool.Search(ip);
                string location = ipTool.Province + " " + ipTool.City;

                //日志服务插入一条操作记录即可

                var logEntity = new OperationLogEntity();

                logEntity.OperIp = ip;
                //logEntity.OperLocation = location;
                logEntity.OperType = logAttribute.OperType.GetHashCode();
                logEntity.Title = logAttribute.Title;
                logEntity.RequestMethod = context.HttpContext.Request.Method;
                logEntity.Method = context.HttpContext.Request.Path.Value;
                logEntity.IsDeleted = false;
                logEntity.OperUser= context.HttpContext.GetUserNameInfo();
                logEntity.OperLocation = location;
                if (logAttribute.IsSaveResponseData)
                {
                    if (context.Result is ContentResult result && result.ContentType == "application/json")
                    {
                        logEntity.RequestResult = result.Content?.Replace("\r\n", "").Trim();
                    }
                   if (context.Result is JsonResult result2)
                    {
                        logEntity.RequestResult = result2.Value?.ToString();
                    }
                  
                    if (context.Result is ObjectResult result3)
                    {
                        logEntity.RequestResult = JsonHelper.ObjToStr(result3.Value);
                    }

                }

                if (logAttribute.IsSaveRequestData)
                {
                    logEntity.RequestParam = context.HttpContext.GetRequestValue(logEntity.RequestMethod);
                }

                _operationLogService._repository.InsertReturnSnowflakeId(logEntity);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"操作日志记录错误:{ex.Message}");
            }

        }
    }
}
