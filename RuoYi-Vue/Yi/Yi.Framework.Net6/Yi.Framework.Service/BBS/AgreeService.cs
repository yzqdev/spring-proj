using SqlSugar;
using System.Threading.Tasks;
using Yi.Framework.Interface.BBS;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.BBS
{
    public partial class AgreeService : BaseService<AgreeEntity>, IAgreeService
    {
        public AgreeService(IRepository<AgreeEntity> repository) : base(repository)
        {
        }
        /// <summary>
        /// 点赞操作
        /// </summary>
        /// <returns></returns>
        public async Task<bool> OperateAsync(long articleOrCommentId, long userId)
        {
            var _articleRepositoty = _repository.ChangeRepository<Repository<ArticleEntity>>();
            var article = await _articleRepositoty.GetByIdAsync(articleOrCommentId);
            if (await _repository.IsAnyAsync(u => u.UserId == userId && u.ArticleOrCommentId == articleOrCommentId))
            {
                //已点赞，取消点赞
                await _repository.UseTranAsync(async () =>
                {
                    await _repository.DeleteAsync(u => u.UserId == userId && u.ArticleOrCommentId == articleOrCommentId);
                    await _articleRepositoty.UpdateIgnoreNullAsync(new ArticleEntity { Id = articleOrCommentId, AgreeNum = article.AgreeNum - 1 });

                });
                return false;
            }
            else
            {
                //未点赞，添加点赞记录
                await _repository.UseTranAsync(async () =>
                {
                    await _repository.InsertReturnSnowflakeIdAsync(new AgreeEntity { UserId = userId, ArticleOrCommentId = articleOrCommentId });
                    await _articleRepositoty.UpdateIgnoreNullAsync(new ArticleEntity { Id = articleOrCommentId, AgreeNum = article.AgreeNum + 1 });
                });
                return true;
            }
        }
    }
}
