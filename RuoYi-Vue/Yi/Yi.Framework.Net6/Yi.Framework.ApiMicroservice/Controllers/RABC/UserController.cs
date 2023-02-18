using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.Base.Dto;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    /// <summary>
    /// 用户管理
    /// </summary>
    [ApiController]
    [Authorize]
    [Route("api/[controller]/[action]")]
    public class UserController : BaseSimpleRdController<UserEntity>
    {
        private IUserService _iUserService;

        public UserController(ILogger<UserEntity> logger, IUserService iUserService) : base(logger, iUserService)
        {
            _iUserService = iUserService;

        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="user"></param>
        /// <param name="page"></param>
        /// <param name="deptId"></param>
        /// <returns></returns>
        [HttpGet]
        [Permission("system:user:query")]
        public async Task<Result> PageList([FromQuery] UserEntity user, [FromQuery] PageParModel page, [FromQuery] long? deptId)
        {
            return Result.Success().SetData(await _iUserService.SelctPageList(user, page, deptId));
        }

        /// <summary>
        /// 更改用户状态
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="isDel"></param>
        /// <returns></returns>
        [HttpPut]
        [Permission("system:user:edit")]
        [Log("用户模块", OperEnum.Update)]
        public async Task<Result> UpdateStatus(long userId, bool isDel)
        {
            return Result.Success().SetData(await _repository.UpdateIgnoreNullAsync(new UserEntity() { Id = userId, IsDeleted = isDel }));
        }

        /// <summary>
        /// 给多用户设置多角色
        /// </summary>
        /// <param name="giveUserSetRoleDto"></param>
        /// <returns></returns>
        [HttpPut]
        [Permission("system:user:edit")]
        [Log("用户模块", OperEnum.Update)]
        public async Task<Result> GiveUserSetRole(GiveUserSetRoleDto giveUserSetRoleDto)
        {
            return Result.Success().SetStatus(await _iUserService.GiveUserSetRole(giveUserSetRoleDto.UserIds, giveUserSetRoleDto.RoleIds));
        }


        /// <summary>
        /// 通过用户id得到用户信息关联部门、岗位、角色
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("{id}")]
        [Permission("system:user:query")]
        public override async Task<Result> GetById([FromRoute] long id)
        {
            return Result.Success().SetData(await _iUserService.GetInfoById(id));
        }

        /// <summary>
        /// 更新用户信息
        /// </summary>
        /// <param name="userDto"></param>
        /// <returns></returns>
        [HttpPut]
        [Permission("system:user:edit")]
        [Log("用户模块", OperEnum.Update)]
        public async Task<Result> Update(UserInfoDto userDto)
        {
            if (await _repository.IsAnyAsync(u => userDto.User!.UserName!.Equals(u.UserName) && !userDto.User.Id.Equals(u.Id)))
            {
                return Result.Error("用户名已存在，修改失败！");
            }
            return Result.Success().SetStatus(await _iUserService.UpdateInfo(userDto));
        }


        /// <summary>
        /// 更新个人中心信息
        /// </summary>
        /// <param name="userDto"></param>
        /// <returns></returns>
        [HttpPut]
        [Permission("system:user:edit")]
        [Log("用户模块", OperEnum.Update)]
        public async Task<Result> UpdateProfile(UserInfoDto userDto)
        {
            //修改需要赋值上主键哦
            userDto.User!.Id = HttpContext.GetUserIdInfo();
            return Result.Success().SetStatus(await _iUserService.UpdateProfile(userDto));
        }

        /// <summary>
        /// 添加用户
        /// </summary>
        /// <param name="userDto"></param>
        /// <returns></returns>
        [HttpPost]
        [Permission("system:user:add")]
        [Log("用户模块", OperEnum.Insert)]
        public async Task<Result> Add(UserInfoDto userDto)
        {
            if (string.IsNullOrEmpty(userDto?.User?.Password))
            {
                return Result.Error("密码为空，添加失败！");
            }
            if (await _repository.IsAnyAsync(u => userDto.User.UserName!.Equals(u.UserName)))
            {
                return Result.Error("用户已经存在，添加失败！");
            }

            return Result.Success().SetStatus(await _iUserService.AddInfo(userDto));
        }

        /// <summary>
        /// 重置密码
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPut]
        [Permission("system:user:edit")]
        [Log("用户模块", OperEnum.Update)]
        public async Task<Result> RestPassword(UserEntity user)
        {
            return Result.Success().SetStatus(await _iUserService.RestPassword(user.Id, user.Password));
        }
        [Permission("system:user:query")]
        public override Task<Result> GetList()
        {
            return base.GetList();
        }
        [Permission("system:user:remove")]
        [Log("用户模块", OperEnum.Delete)]
        public override Task<Result> DelList(List<long> ids)
        {
            return base.DelList(ids);
        }
    }
}
