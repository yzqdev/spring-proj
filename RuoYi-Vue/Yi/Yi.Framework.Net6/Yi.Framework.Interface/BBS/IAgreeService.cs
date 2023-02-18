using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.BBS
{
    public partial interface IAgreeService : IBaseService<AgreeEntity>
    {
        Task<bool> OperateAsync(long articleOrCommentId, long userId);

    }
}
