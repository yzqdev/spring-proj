using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.RABC
{
    public partial interface ILoginLogService : IBaseService<LoginLogEntity>
    {
        Task<PageModel<List<LoginLogEntity>>> SelctPageList(LoginLogEntity loginLog, PageParModel page);
    }
}
