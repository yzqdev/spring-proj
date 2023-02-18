using SqlSugar;
using Yi.Framework.Interface.SHOP;
using Yi.Framework.Model.SHOP.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.SHOP
{
    public partial class SpecsGroupService : BaseService<SpecsGroupEntity>, ISpecsGroupService
    {
        public SpecsGroupService(IRepository<SpecsGroupEntity> repository) : base(repository)
        {
        }
    }
}
