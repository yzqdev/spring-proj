using System.Collections.Generic;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.Base.Dto;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;

namespace Yi.Framework.Interface.RABC
{
    public partial interface IRoleService:IBaseService<RoleEntity>
    {
        /// <summary>
        /// DbTest
        /// </summary>
        /// <returns></returns>
        Task<List<RoleEntity>> DbTest();



        /// <summary>
        /// 通过角色id获取角色实体包含菜单
        /// </summary>
        /// <param name="roleId"></param>
        /// <returns></returns>
        Task<RoleEntity> GetInMenuByRoleId(long roleId);

        /// <summary>
        /// 给角色设置菜单，多角色，多菜单
        /// </summary>
        /// <param name="roleIds"></param>
        /// <param name="menuIds"></param>
        /// <returns></returns>
        Task<bool> GiveRoleSetMenu(List<long> roleIds, List<long> menuIds);

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="role"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        Task<PageModel<List<RoleEntity>>> SelctPageList(RoleEntity role, PageParModel page);

        /// <summary>
        /// 添加角色关联菜单
        /// </summary>
        /// <param name="roleDto"></param>
        /// <returns></returns>
        Task<bool> AddInfo(RoleInfoDto roleDto);


        /// <summary>
        /// 更新角色关联菜单
        /// </summary>
        /// <param name="roleDto"></param>
        /// <returns></returns>
        Task<bool> UpdateInfo(RoleInfoDto roleDto);

        /// <summary>
        /// 给角色设置部门
        /// </summary>
        /// <param name="roleIds"></param>
        /// <param name="deptIds"></param>
        /// <returns></returns>
        Task<bool> GiveRoleSetDept(List<long> roleIds, List<long> deptIds);

        /// <summary>
        /// 更新角色数据权限
        /// </summary>
        /// <param name="roleDto"></param>
        /// <returns></returns>
        Task<bool> UpdateDataScpoce(RoleInfoDto roleDto);
    }
}
