using SqlSugar;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.RABC
{
    public partial class UserRoleService : BaseService<UserRoleEntity>, IUserRoleService
    {
        public UserRoleService(IRepository<UserRoleEntity> repository) : base(repository)
        {
        }
    }
}
