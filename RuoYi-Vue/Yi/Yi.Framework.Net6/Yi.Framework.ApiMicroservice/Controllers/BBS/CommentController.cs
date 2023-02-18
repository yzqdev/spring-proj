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
using Yi.Framework.WebCore;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class CommentController : BaseSimpleCrudController<CommentEntity>
    {
        private ICommentService _iCommentService;
        private IMapper _mapper;
        public CommentController(ILogger<CommentEntity> logger, ICommentService iCommentService, IMapper mapper) : base(logger, iCommentService)
        {
            _iCommentService = iCommentService;
            _mapper = mapper;
        }

        /// <summary>
        /// 获取文章的全部一级评论
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [Route("{articleId}")]
        public async Task<Result> GetListByArticleId(long articleId)
        {
            //一级评论被回复的用户id为空
            var data = await _repository._DbQueryable.Where(u => u.ParentId == 0 && u.ArticleId == articleId).Includes(u => u.CreateUserInfo).OrderByDescending(u=>u.CreateTime).ToListAsync();
            return Result.Success().SetData(_mapper.Map<List<CommentVo>>(data));
        }

        /// <summary>
        /// 获取一级评论详情
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public override async Task<Result> GetById([FromRoute] long id)
        {
            var data = await _repository._DbQueryable.Includes(u => u.CreateUserInfo).Includes(u => u.UserInfo).FirstAsync(u => u.Id == id);
            return Result.Success().SetData(_mapper.Map<CommentVo>(data));
        }

        /// <summary>
        /// 回复文章或回复评论
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public override async Task<Result> Add(CommentEntity comment)
        {
           return Result.Success().SetStatus(await _iCommentService.AddAsync(comment));
        }
    }
}
