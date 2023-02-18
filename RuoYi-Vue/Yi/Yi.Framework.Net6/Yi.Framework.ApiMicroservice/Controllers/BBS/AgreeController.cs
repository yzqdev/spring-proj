using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Org.BouncyCastle.Asn1.IsisMtt.X509;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.BBS;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class AgreeController : ControllerBase
    {
        [Autowired]
        public IAgreeService? _iAgreeService { get; set; }
        [Autowired]
        public IArticleService? _iArticleService { get; set; }
        [Autowired]
        public ILogger<AgreeEntity>? _logger { get; set; }

        /// <summary>
        /// 点赞操作
        /// </summary>
        /// <param name="articleId"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> Operate(long articleId)
        {
            long userId = HttpContext.GetUserIdInfo();
            if (await _iAgreeService!.OperateAsync(articleId, userId))
            {
                return Result.Success("点赞成功");
              
            }
            else
            {
                return Result.Success("已点赞，取消点赞").StatusFalse();
            }
        }
    }
}
