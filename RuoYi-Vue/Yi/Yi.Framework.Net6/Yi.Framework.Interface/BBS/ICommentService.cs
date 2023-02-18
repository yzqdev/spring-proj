using System.Threading.Tasks;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.BBS
{
    public partial interface ICommentService:IBaseService<CommentEntity>
    {
        Task<bool> AddAsync(CommentEntity comment);
    }
}
