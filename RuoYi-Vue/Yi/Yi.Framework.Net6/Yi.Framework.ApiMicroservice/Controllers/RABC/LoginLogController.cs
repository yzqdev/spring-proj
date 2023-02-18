using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.WebCore;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class LoginLogController : BaseSimpleCrudController<LoginLogEntity>
    {
        private ILoginLogService _iLoginLogService;
        public LoginLogController(ILogger<LoginLogEntity> logger, ILoginLogService iLoginLogService) : base(logger, iLoginLogService)
        {
            _iLoginLogService = iLoginLogService;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="loginLog"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] LoginLogEntity loginLog, [FromQuery] PageParModel page)
        {
            return Result.Success().SetData(await _iLoginLogService.SelctPageList(loginLog, page));
        }

    }
}
