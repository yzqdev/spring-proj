using SqlSugar;
using System.Collections.Generic;
using System.Threading.Tasks;
using System;
using Yi.Framework.Common.Base;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;
using Yi.Framework.Interface.RABC;
using Yi.Framework.DtoModel.Base.Dto;

namespace Yi.Framework.Service.RABC
{
    public partial class RoleService : BaseService<RoleEntity>, IRoleService
    {
        public RoleService(IRepository<RoleEntity> repository) : base(repository)
        {
        }
        public async Task<List<RoleEntity>> DbTest()
        {
            return await _repository._DbQueryable.ToListAsync();
        }



        public async Task<bool> GiveRoleSetMenu(List<long> roleIds, List<long> menuIds)
        {
            var _repositoryRoleMenu = _repository.ChangeRepository<Repository<RoleMenuEntity>>();
            //多次操作，需要事务确保原子性
            return await _repositoryRoleMenu.UseTranAsync(async () =>
            {   //删除用户之前所有的用户角色关系（物理删除，没有恢复的必要）
                await _repositoryRoleMenu.DeleteAsync(u => roleIds.Contains((long)u.RoleId));

                //遍历用户
                foreach (var roleId in roleIds)
                {
                    //添加新的关系
                    List<RoleMenuEntity> roleMenuEntity = new();
                    foreach (var menu in menuIds)
                    {
                        roleMenuEntity.Add(new RoleMenuEntity() { RoleId = roleId, MenuId = menu });
                    }

                    //一次性批量添加
                    await _repositoryRoleMenu.InsertReturnSnowflakeIdAsync(roleMenuEntity);
                }
            });


        }

        public async Task<RoleEntity> GetInMenuByRoleId(long roleId)
        {
            return await _repository._Db.Queryable<RoleEntity>().Includes(u => u.Menus).InSingleAsync(roleId);

        }



        public async Task<PageModel<List<RoleEntity>>> SelctPageList(RoleEntity role, PageParModel page)
        {
            RefAsync<int> total = 0;
            var data = await _repository._DbQueryable
                    .WhereIF(!string.IsNullOrEmpty(role.RoleName), u => u.RoleName.Contains(role.RoleName))
                     .WhereIF(!string.IsNullOrEmpty(role.RoleCode), u => u.RoleCode.Contains(role.RoleCode))
                    .WhereIF(page.StartTime.IsNotNull() && page.EndTime.IsNotNull(), u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                     .WhereIF(role.IsDeleted.IsNotNull(), u => u.IsDeleted == role.IsDeleted)
                    .OrderBy(u => u.OrderNum, OrderByType.Desc)
                    .ToPageListAsync(page.PageNum, page.PageSize, total);

            return new PageModel<List<RoleEntity>>(data, total);
        }



        public async Task<bool> AddInfo(RoleInfoDto roleDto)
        {
            var res1 = await _repository.InsertReturnSnowflakeIdAsync(roleDto.Role);
            var res2 = await GiveRoleSetMenu(new List<long> { res1 }, roleDto.MenuIds);
            return !0.Equals(res1) && res2;
        }

        public async Task<bool> GiveRoleSetDept(List<long> roleIds, List<long> deptIds)
        {
            var _repositoryRoleDept = _repository.ChangeRepository<Repository<RoleDeptEntity>>();
            //多次操作，需要事务确保原子性
            return await _repositoryRoleDept.UseTranAsync(async () =>
            {   //删除用户之前所有的用户角色关系（物理删除，没有恢复的必要）
                await _repositoryRoleDept.DeleteAsync(u => roleIds.Contains((long)u.RoleId));

                //遍历角色
                foreach (var roleId in roleIds)
                {
                    //添加新的关系
                    List<RoleDeptEntity> roleDeptEntity = new();
                    foreach (var dept in deptIds)
                    {
                        roleDeptEntity.Add(new RoleDeptEntity() { RoleId = roleId, DeptId = dept });
                    }

                    //一次性批量添加
                    await _repositoryRoleDept.InsertReturnSnowflakeIdAsync(roleDeptEntity);
                }
            });
        }

        public async Task<bool> UpdateInfo(RoleInfoDto roleDto)
        {
            var res1 = await _repository.UpdateIgnoreNullAsync(roleDto.Role);
            var res2 = await GiveRoleSetMenu(new List<long> { roleDto.Role.Id }, roleDto.MenuIds);
            var res3 = await GiveRoleSetDept(new List<long> { roleDto.Role.Id }, roleDto.DeptIds);
            return res1 && res2 && res3;
        }

        public async Task<bool> UpdateDataScpoce(RoleInfoDto roleDto)
        {
            var role = new RoleEntity();
            role.Id = roleDto.Role.Id;
            role.DataScope = roleDto.Role.DataScope;
            var res1 = await _repository.UpdateIgnoreNullAsync(role);
            var res3 = await GiveRoleSetDept(new List<long> { roleDto.Role.Id }, roleDto.DeptIds);
            return res1 && res3;
        }
    }
}
