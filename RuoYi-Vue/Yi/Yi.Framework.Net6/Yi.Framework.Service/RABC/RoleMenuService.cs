using SqlSugar;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.RABC
{
    public partial class RoleMenuService : BaseService<RoleMenuEntity>, IRoleMenuService
    {
        public RoleMenuService(IRepository<RoleMenuEntity> repository) : base(repository)
        {
        }
    }
}
