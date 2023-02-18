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
    public partial class DictionaryService : BaseService<DictionaryEntity>, IDictionaryService
    {
        public DictionaryService(IRepository<DictionaryEntity> repository) : base(repository)
        {
        }
        public async Task<PageModel<List<DictionaryEntity>>> SelctPageList(DictionaryEntity dic, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(dic.DictName), u => u.DictName.Contains(dic.DictName))
                     .WhereIF(!string.IsNullOrEmpty(dic.DictType), u => u.DictType.Contains(dic.DictType))
                    .WhereIF(page.StartTime is not null && page.EndTime is not null, u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                     .Where(u => u.IsDeleted == false)
                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                    .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<DictionaryEntity>>(data, total);
        }
    }
}
