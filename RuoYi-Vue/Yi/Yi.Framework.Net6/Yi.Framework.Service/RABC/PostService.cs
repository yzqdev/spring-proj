using SqlSugar;
using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.RABC
{
    public partial class PostService : BaseService<PostEntity>, IPostService
    {
        public PostService(IRepository<PostEntity> repository) : base(repository)
        {
        }

        public async Task<PageModel<List<PostEntity>>> SelctPageList(PostEntity post, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(post.PostName), u => u.PostName.Contains(post.PostName))
                        .WhereIF(!string.IsNullOrEmpty(post.PostCode), u => u.PostCode.Contains(post.PostCode))
                     .WhereIF(post.IsDeleted is not null, u => u.IsDeleted == post.IsDeleted)

                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                    .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<PostEntity>>(data, total);
        }
    }
}
