using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.RABC
{
    public partial interface IDeptService:IBaseService<DeptEntity>
    {
        /// <summary>
        /// 动态条件查询
        /// </summary>
        /// <param name="dept"></param>
        /// <returns></returns>
        Task<List<DeptEntity>> SelctGetList(DeptEntity dept);


        /// <summary>
        /// 根据角色id获取该角色的部门权限
        /// </summary>
        /// <param name="roleId"></param>
        /// <returns></returns>
        Task<List<DeptEntity>> GetListByRoleId(long roleId);
    }
}
