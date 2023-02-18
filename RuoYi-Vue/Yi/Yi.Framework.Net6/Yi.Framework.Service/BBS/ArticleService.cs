using SqlSugar;
using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.BBS;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.BBS
{
    public partial class ArticleService : BaseService<ArticleEntity>, IArticleService
    {
        public ArticleService(IRepository<ArticleEntity> repository) : base(repository)
        {
        }
        public async Task<PageModel<List<ArticleEntity>>> SelctPageList(ArticleEntity entity, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                .Includes(x => x.User)
                       //.WhereIF(!string.IsNullOrEmpty(config.ConfigName), u => u.ConfigName.Contains(config.ConfigName))
                       //.WhereIF(!string.IsNullOrEmpty(config.ConfigKey), u => u.ConfigKey.Contains(config.ConfigKey))
                       .WhereIF(page.StartTime is not null && page.EndTime is not null, u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                     .WhereIF(entity.IsDeleted is not null, u => u.IsDeleted == entity.IsDeleted)
                    .OrderBy(u => u.CreateTime, OrderByType.Desc)
                          .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<ArticleEntity>>(data, total);
        }
    }
}
