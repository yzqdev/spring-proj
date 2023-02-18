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
    public partial class LoginLogService : BaseService<LoginLogEntity>, ILoginLogService
    {
        public LoginLogService(IRepository<LoginLogEntity> repository) : base(repository)
        {
        }

        public async Task<PageModel<List<LoginLogEntity>>> SelctPageList(LoginLogEntity loginLog, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(loginLog.LoginIp), u => u.LoginIp.Contains(loginLog.LoginIp))
                        .WhereIF(!string.IsNullOrEmpty(loginLog.LoginUser), u => u.LoginUser.Contains(loginLog.LoginUser))
                     .WhereIF(loginLog.IsDeleted is not null, u => u.IsDeleted == loginLog.IsDeleted)
                     .WhereIF(page.StartTime is not null && page.EndTime is not null, u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                    .OrderBy(u => u.CreateTime, OrderByType.Desc)
                    .ToPageListAsync(page.PageNum, page.PageSize, total);
            return new PageModel<List<LoginLogEntity>>(data, total);
        }
    }
}
