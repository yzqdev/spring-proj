using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.RABC
{
    public partial interface IPostService:IBaseService<PostEntity>
    {
        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="post"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        Task<PageModel<List<PostEntity>>> SelctPageList(PostEntity post, PageParModel page);
    }
}
