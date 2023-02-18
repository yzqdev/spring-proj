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
    public partial class DictionaryInfoService : BaseService<DictionaryInfoEntity>, IDictionaryInfoService
    {
        public DictionaryInfoService(IRepository<DictionaryInfoEntity> repository) : base(repository)
        {
        }
        public async Task<PageModel<List<DictionaryInfoEntity>>> SelctPageList(DictionaryInfoEntity dicInfo, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(dicInfo.DictLabel), u => u.DictLabel.Contains(dicInfo.DictLabel))
                     .WhereIF(!string.IsNullOrEmpty(dicInfo.DictType), u => u.DictType.Contains(dicInfo.DictType))
                     .Where(u => u.IsDeleted == false)
                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                    .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<DictionaryInfoEntity>>(data, total);
        }
    }
}