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
    public class PostController : BaseSimpleCrudController<PostEntity>
    {
        private IPostService _iPostService;
        public PostController(ILogger<PostEntity> logger, IPostService iPostService) : base(logger, iPostService)
        {
            _iPostService = iPostService;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="post"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] PostEntity post, [FromQuery] PageParModel page)
        {
            return Result.Success().SetData(await _iPostService.SelctPageList(post, page));
        }



        public override async Task<Result> Add(PostEntity entity)
        {
            return await base.Add(entity);
        }

        public override async Task<Result> Update(PostEntity entity)
        {
            return await base.Update(entity);
        }
    }
}
