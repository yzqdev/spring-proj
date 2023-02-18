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
    public partial class OperationLogService : BaseService<OperationLogEntity>, IOperationLogService
    {
        public OperationLogService(IRepository<OperationLogEntity> repository) : base(repository)
        {
        }
        public async Task<PageModel<List<OperationLogEntity>>> SelctPageList(OperationLogEntity operationLog, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(operationLog.Title), u => u.Title.Contains(operationLog.Title))
                        .WhereIF(!string.IsNullOrEmpty(operationLog.OperUser), u => u.OperUser.Contains(operationLog.OperUser))
                        .WhereIF(operationLog.OperType is not null, u => u.OperType == operationLog.OperType.GetHashCode())
                     .WhereIF(operationLog.IsDeleted is not null, u => u.IsDeleted == operationLog.IsDeleted)
                     .WhereIF(page.StartTime is not null && page.EndTime is not null, u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                    .OrderBy(u => u.CreateTime, OrderByType.Desc)
                    .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<OperationLogEntity>>(data, total);
        }
    }
}
