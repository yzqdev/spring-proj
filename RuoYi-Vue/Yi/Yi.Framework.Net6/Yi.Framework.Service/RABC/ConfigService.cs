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
    public partial class ConfigService : BaseService<ConfigEntity>, IConfigService
    {
        public ConfigService(IRepository<ConfigEntity> repository) : base(repository)
        {
        }
        public async Task<PageModel<List<ConfigEntity>>> SelctPageList(ConfigEntity config, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(config.ConfigName), u => u.ConfigName.Contains(config.ConfigName))
                    .WhereIF(!string.IsNullOrEmpty(config.ConfigKey), u => u.ConfigKey.Contains(config.ConfigKey))
                       .WhereIF(page.StartTime is not null && page.EndTime is not null, u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                     .WhereIF(config.IsDeleted is not null, u => u.IsDeleted == config.IsDeleted)
                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                          .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<ConfigEntity>>(data, total);
        }
    }
}
