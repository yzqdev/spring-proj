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
    public partial class DeptService : BaseService<DeptEntity>, IDeptService
    {
        public DeptService(IRepository<DeptEntity> repository) : base(repository)
        {
        }
        public async Task<List<DeptEntity>> SelctGetList(DeptEntity dept)
        {
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(dept.DeptName), u => u.DeptName.Contains(dept.DeptName))
                     .WhereIF(dept.IsDeleted is not null, u => u.IsDeleted == dept.IsDeleted)
                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                   .ToListAsync();

            return data;
        }

        public async Task<List<DeptEntity>> GetListByRoleId(long roleId)
        {
            return (await _repository._Db.Queryable<RoleEntity>().Includes(r => r.Depts).SingleAsync(r => r.Id == roleId)).Depts;
        }
    }
}
