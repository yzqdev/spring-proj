using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.Base.Vo;
using Yi.Framework.Interface;
using Yi.Framework.Interface.BBS;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    /// <summary>
    /// 文章控制器
    /// </summary>
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class ArticleController : BaseSimpleCrudController<ArticleEntity>
    {
        private IArticleService _iArticleService;
        private IMapper _mapper;
        public ArticleController(ILogger<ArticleEntity> logger, IArticleService iArticleService, IMapper mapper) : base(logger, iArticleService)
        {
            _iArticleService = iArticleService;
            _mapper = mapper;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="entity"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] ArticleEntity entity, [FromQuery] PageParModel page)
        {
            var pageData = await _iArticleService.SelctPageList(entity, page);
            return Result.Success().SetData(new PageModel() { Data = _mapper.Map<List<ArticleVo>>(pageData.Data), Total = pageData.Total });
        }

        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public override Task<Result> Add(ArticleEntity entity)
        {
            //如果标题为空，默认为内容的前20个字符
            entity.Title = string.IsNullOrEmpty(entity.Title) ? 
                (entity.Content?.Length > 20 ? entity.Content.Substring(0, 20) : entity.Content) :
                entity.Title;
            entity.UserId = HttpContext.GetUserIdInfo();
            return base.Add(entity);
        }
    }
}
