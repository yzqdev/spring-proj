using SqlSugar;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.RABC
{
    public partial class UserPostService : BaseService<UserPostEntity>, IUserPostService
    {
        public UserPostService(IRepository<UserPostEntity> repository) : base(repository)
        {
        }
    }
}
