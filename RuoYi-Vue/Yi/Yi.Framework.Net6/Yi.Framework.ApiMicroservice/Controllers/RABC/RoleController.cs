using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.Base.Dto;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service;
using Yi.Framework.WebCore;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    /// <summary>
    /// 角色管理
    /// </summary>
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class RoleController : BaseSimpleRdController<RoleEntity>
    {
        private IRoleService _iRoleService;
        public RoleController(ILogger<RoleEntity> logger, IRoleService iRoleService) : base(logger, iRoleService)
        {
            _iRoleService = iRoleService;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <returns></returns>
        [Permission("system:role:query")]
        [HttpGet]
        public async Task<Result> PageList([FromQuery] RoleEntity role, [FromQuery] PageParModel page)
        {
            return Result.Success().SetData(await _iRoleService.SelctPageList(role, page));
        }


        /// <summary>
        /// 给多用户设置多角色
        /// </summary>
        /// <param name="giveRoleSetMenuDto"></param>
        /// <returns></returns>
        [HttpPut]
        [Permission("system:role:edit")]
        public async Task<Result> GiveRoleSetMenu(GiveRoleSetMenuDto giveRoleSetMenuDto)
        {
            return Result.Success().SetStatus(await _iRoleService.GiveRoleSetMenu(giveRoleSetMenuDto.RoleIds, giveRoleSetMenuDto.MenuIds));
        }


        /// <summary>
        /// 添加角色包含菜单
        /// </summary>
        /// <param name="roleDto"></param>
        /// <returns></returns>
        [Permission("system:role:add")]
        [HttpPost]
        public async Task<Result> Add(RoleInfoDto roleDto)
        {
            return Result.Success().SetData(await _iRoleService.AddInfo(roleDto));
        }

        /// <summary>
        /// 更新角色信息
        /// </summary>
        /// <returns></returns>
        [Permission("system:role:edit")]
        [HttpPut]
        public async Task<Result> Update(RoleInfoDto roleDto)
        {
            return Result.Success().SetStatus(await _iRoleService.UpdateInfo(roleDto));
        }

        /// <summary>
        /// 更改角色状态
        /// </summary>
        /// <param name="roleId"></param>
        /// <param name="isDel"></param>
        /// <returns></returns>
        [Permission("system:role:edit")]
        [HttpPut]
        public async Task<Result> UpdateStatus(long roleId, bool isDel)
        {
            return Result.Success().SetData(await _iRoleService._repository.UpdateIgnoreNullAsync(new RoleEntity() { Id = roleId, IsDeleted = isDel }));
        }

        /// <summary>
        ///更改角色数据权限
        /// </summary>
        /// <returns></returns>
        [Permission("system:role:edit")]
        [HttpPut]
        public async Task<Result> UpdateDataScpoce(RoleInfoDto roleDto)
        {
            return Result.Success().SetStatus(await _iRoleService.UpdateDataScpoce(roleDto));
        }

        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="ids"></param>
        /// <returns></returns>
        [Permission("system:role:remove")]
        [HttpDelete]
        public override async Task<Result> DelList(List<long> ids)
        {
            return await base.DelList(ids);
        }
    }
}
