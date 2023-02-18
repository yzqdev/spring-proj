using SqlSugar;
using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.RABC
{
    public partial class MenuService : BaseService<MenuEntity>, IMenuService
    {
        public MenuService(IRepository<MenuEntity> repository) : base(repository)
        {
        }
        public async Task<List<MenuEntity>> SelctGetList(MenuEntity menu)
        {
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(menu.MenuName), u => u.MenuName.Contains(menu.MenuName))
                     .Where(u => u.IsDeleted == false)
                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                    .ToListAsync();
            return data;
        }
        public async Task<List<MenuEntity>> GetMenuTreeAsync()
        {
            //ParentId 0,代表为根目录，只能存在一个
            //复杂查询直接使用db代理
            return await _repository._Db.Queryable<MenuEntity>().Where(u => u.IsDeleted == false).ToTreeAsync(it => it.Children, it => it.ParentId, 0);
        }


        public async Task<List<MenuEntity>> GetListByRoleId(long roleId)
        {
            return (await _repository._Db.Queryable<RoleEntity>().Includes(r => r.Menus).SingleAsync(r => r.Id == roleId)).Menus;
        }
    }
}
