using System.Collections.Generic;
using System.Threading.Tasks;
using System;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface.Base;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.DtoModel.Base.Dto;

namespace Yi.Framework.Interface.RABC
{
    public partial interface IUserService:IBaseService<UserEntity>
    {

        /// <summary>
        /// 关联角色测试
        /// </summary>
        /// <returns></returns>
        Task<List<UserEntity>> GetListInRole();

        /// <summary>
        /// 测试仓储的上下文对象
        /// </summary>
        /// <returns></returns>
        Task<List<UserEntity>> DbTest();

        /// <summary>
        /// 登录方法
        /// </summary>
        /// <param name="userName"></param>
        /// <param name="password"></param>
        /// <param name="userAction"></param>
        /// <returns></returns>
        Task<bool> Login(string userName, string password, Action<UserEntity> userAction = null);

        /// <summary>
        /// 注册方法
        /// </summary>
        /// <param name="userEntity"></param>
        /// <param name="userAction"></param>
        /// <returns></returns>
        Task<bool> Register(UserEntity userEntity, Action<UserEntity> userAction = null);

        /// <summary>
        /// 导航属性关联角色、部门、岗位
        /// </summary>
        /// <returns></returns>
        Task<UserEntity> GetInfoById(long userId);

        /// <summary>
        /// 给用户设置角色，多用户，多角色
        /// </summary>
        /// <param name="userIds"></param>
        /// <param name="roleIds"></param>
        /// <returns></returns>
        Task<bool> GiveUserSetRole(List<long> userIds, List<long> roleIds);

        /// <summary>
        /// 判断用户名是否存在，如果存在可返回该用户
        /// </summary>
        /// <param name="userName"></param>
        /// <param name="userAction"></param>
        /// <returns></returns>
        Task<bool> Exist(string userName, Action<UserEntity> userAction = null);

        /// <summary>
        /// 获取当前登录用户的所有信息
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        Task<UserRoleMenuDto> GetUserAllInfo(long userId);

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="user"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        Task<PageModel<List<UserEntity>>> SelctPageList(UserEntity user, PageParModel page, long? deptId);


        /// <summary>
        /// 更新用户信息
        /// </summary>
        /// <param name="userDto"></param>
        /// <returns></returns>
        Task<bool> UpdateInfo(UserInfoDto userDto);

        /// <summary>
        /// 添加用户信息
        /// </summary>
        /// <param name="userDto"></param>
        /// <returns></returns>
        Task<bool> AddInfo(UserInfoDto userDto);

        /// <summary>
        /// 重置密码
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        Task<bool> RestPassword(long userId, string password);


        /// <summary>
        /// 给用户设置岗位
        /// </summary>
        /// <param name="userIds"></param>
        /// <param name="postIds"></param>
        /// <returns></returns>
        Task<bool> GiveUserSetPost(List<long> userIds, List<long> postIds);


        /// <summary>
        /// 更新密码
        /// </summary>
        /// <param name="dto"></param>
        /// <param name="userId"></param>
        /// <returns></returns>
        Task<bool> UpdatePassword(UpdatePasswordDto dto, long userId);

        /// <summary>
        /// 个人中心信息更新
        /// </summary>
        /// <param name="userDto"></param>
        /// <returns></returns>
        Task<bool> UpdateProfile(UserInfoDto userDto);
    }
}
