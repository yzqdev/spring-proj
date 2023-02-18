using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.BBS
{
    public partial interface IArticleService:IBaseService<ArticleEntity>
    {
        Task<PageModel<List<ArticleEntity>>> SelctPageList(ArticleEntity eneity, PageParModel page);
    }
  
}
