using SqlSugar;
using Yi.Framework.Interface.SHOP;
using Yi.Framework.Model.SHOP.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;

namespace Yi.Framework.Service.SHOP
{
    public partial class CategoryService : BaseService<CategoryEntity>, ICategoryService
    {
        public CategoryService(IRepository<CategoryEntity> repository) : base(repository)
        {
        }
    }
}
