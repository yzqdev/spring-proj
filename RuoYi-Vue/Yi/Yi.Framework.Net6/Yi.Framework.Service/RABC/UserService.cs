using SqlSugar;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System;
using Yi.Framework.Common.Base;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base;
using Yi.Framework.Interface.RABC;
using Yi.Framework.DtoModel.Base.Dto;

namespace Yi.Framework.Service.RABC
{
    public partial class UserService : BaseService<UserEntity>, IUserService
    {
        public UserService(IRepository<UserEntity> repository) : base(repository)
        {
        }
        public async Task<List<UserEntity>> GetListInRole()
        {
            return await _repository._DbQueryable.Includes(u => u.Roles).ToListAsync();
        }
        public async Task<List<UserEntity>> DbTest()
        {
            return await _repository._Db.Queryable<UserEntity>().ToListAsync();
        }
        public async Task<bool> Exist(long id, Action<UserEntity> userAction = null)
        {
            var user = await _repository.GetByIdAsync(id);
            userAction.Invoke(user);
            if (user == null)
            {
                return false;
            }
            return true;
        }
        public async Task<bool> Exist(string userName, Action<UserEntity> userAction = null)
        {
            var user = await _repository.GetFirstAsync(u => u.UserName == userName && u.IsDeleted == false);
            if (userAction != null)
            {
                userAction.Invoke(user);
            }
            if (user == null)
            {
                return false;
            }
            return true;
        }
        public async Task<bool> Login(string userName, string password, Action<UserEntity> userAction = null)
        {
            var user = new UserEntity();
            if (await Exist(userName, o => user = o))
            {
                userAction.Invoke(user);
                if (user.Password == MD5Helper.SHA2Encode(password, user.Salt))
                {
                    return true;
                }
            }
            return false;
        }

        public async Task<bool> Register(UserEntity userEntity, Action<UserEntity> userAction = null)
        {
            var user = new UserEntity();
            if (!await Exist(userEntity.UserName))
            {
                user.UserName = userEntity.UserName;
                user.BuildPassword();
                userAction.Invoke(await _repository.InsertReturnEntityAsync(user));
                return true;
            }
            return false;
        }

        public async Task<bool> GiveUserSetRole(List<long> userIds, List<long> roleIds)
        {
            var _repositoryUserRole = _repository.ChangeRepository<Repository<UserRoleEntity>>();

            //多次操作，需要事务确保原子性
            return await _repositoryUserRole.UseTranAsync(async () =>
            {
                //删除用户之前所有的用户角色关系（物理删除，没有恢复的必要）
                await _repositoryUserRole.DeleteAsync(u => userIds.Contains((long)u.UserId));

                if (roleIds is not null)
                {
                    //遍历用户
                    foreach (var userId in userIds)
                    {
                        //添加新的关系
                        List<UserRoleEntity> userRoleEntities = new();

                        foreach (var roleId in roleIds)
                        {
                            userRoleEntities.Add(new UserRoleEntity() { UserId = userId, RoleId = roleId });
                        }

                        //一次性批量添加
                        await _repositoryUserRole.InsertReturnSnowflakeIdAsync(userRoleEntities);
                    }
                }


            });
        }


        public async Task<bool> GiveUserSetPost(List<long> userIds, List<long> postIds)
        {
            var _repositoryUserPost = _repository.ChangeRepository<Repository<UserPostEntity>>();

            //多次操作，需要事务确保原子性
            return await _repositoryUserPost.UseTranAsync(async () =>
            {
                //删除用户之前所有的用户角色关系（物理删除，没有恢复的必要）
                await _repositoryUserPost.DeleteAsync(u => userIds.Contains((long)u.UserId));
                if (postIds is not null)
                {
                    //遍历用户
                    foreach (var userId in userIds)
                    {
                        //添加新的关系
                        List<UserPostEntity> userPostEntities = new();
                        foreach (var post in postIds)
                        {
                            userPostEntities.Add(new UserPostEntity() { UserId = userId, PostId = post });
                        }

                        //一次性批量添加
                        await _repositoryUserPost.InsertReturnSnowflakeIdAsync(userPostEntities);
                    }

                }
            });
        }



        public async Task<UserEntity> GetInfoById(long userId)
        {
            var data = await _repository._DbQueryable.Includes(u => u.Roles).Includes(u => u.Posts).Includes(u => u.Dept).InSingleAsync(userId);
            data.Password = null;
            data.Salt = null;
            return data;
        }

        public async Task<UserRoleMenuDto> GetUserAllInfo(long userId)
        {


            var userRoleMenu = new UserRoleMenuDto();
            //首先获取到该用户全部信息，导航到角色、菜单，(菜单需要去重,完全交给Set来处理即可)

            //得到用户
            var user = await _repository._DbQueryable.Includes(u => u.Roles.Where(r => r.IsDeleted == false).ToList(), r => r.Menus.Where(m => m.IsDeleted == false).ToList()).InSingleAsync(userId);
            if (user is null)
            {
                return null;
            }
            user.Password = null;
            user.Salt = null;

            //超级管理员特殊处理
            if (SystemConst.Admin.Equals(user.UserName))
            {
                userRoleMenu.User = user;
                userRoleMenu.RoleCodes.Add(SystemConst.AdminRolesCode);
                userRoleMenu.PermissionCodes.Add(SystemConst.AdminPermissionCode);
                return userRoleMenu;
            }



            //得到角色集合
            var roleList = user.Roles;

            //得到菜单集合
            foreach (var role in roleList)
            {
                userRoleMenu.RoleCodes.Add(role.RoleCode);

                if (role.Menus.IsNotNull())
                {
                    foreach (var menu in role.Menus)
                    {


                        if (!string.IsNullOrEmpty(menu.PermissionCode))
                        {
                            userRoleMenu.PermissionCodes.Add(menu.PermissionCode);

                        }

                        userRoleMenu.Menus.Add(menu);
                    }
                }

                //刚好可以去除一下多余的导航属性
                role.Menus = null;
                userRoleMenu.Roles.Add(role);
            }

            user.Roles = null;
            userRoleMenu.User = user;




            return userRoleMenu;
        }




        public async Task<PageModel<List<UserEntity>>> SelctPageList(UserEntity user, PageParModel page, long? deptId)
        {

            RefAsync<int> total = 0;
            List<UserEntity> data = null;
            var query = _repository._DbQueryable
                        .WhereIF(!string.IsNullOrEmpty(user.UserName), u => u.UserName.Contains(user.UserName))
                        .WhereIF(!string.IsNullOrEmpty(user.Name), u => u.Name.Contains(user.Name))
                        .WhereIF(!string.IsNullOrEmpty(user.Phone), u => u.Phone.Contains(user.Phone))
                        .WhereIF(page.StartTime.IsNotNull() && page.EndTime.IsNotNull(), u => u.CreateTime >= page.StartTime && u.CreateTime <= page.EndTime)
                        .WhereIF(user.IsDeleted.IsNotNull(), u => u.IsDeleted == user.IsDeleted)
                        .Includes(u => u.Roles)
                        .Includes(u => u.Posts)
                        .Includes(u => u.Dept);
            if (deptId is not null)
            {
                //如果deptId不为空，部门id以下及自己都可以
                List<long> deptIds = (await _repository._Db.Queryable<DeptEntity>().ToChildListAsync(it => it.ParentId, deptId)).Select(d => d.Id).ToList();
                query = query.Where(u => u.DeptId != null && deptIds.Contains((long)u.DeptId));
            }

            data = await query.OrderBy(u => u.OrderNum, OrderByType.Desc)
             .ToPageListAsync(page.PageNum, page.PageSize, total);


            data.ForEach(u => { u.Password = null; u.Salt = null; });
            return new PageModel<List<UserEntity>>(data, total);
        }




        public async Task<bool> UpdateInfo(UserInfoDto userDto)
        {
            //未填写密码，可不更新
            userDto.User.Salt = null;
            if (userDto.User.Password.IsNotNull())
            {
                userDto.User.BuildPassword();
            }
            userDto.User.DeptId = userDto.DeptId;
            var res1 = await _repository.UpdateIgnoreNullAsync(userDto.User);
            var res2 = await GiveUserSetRole(new List<long> { userDto.User.Id }, userDto.RoleIds);
            var res3 = await GiveUserSetPost(new List<long> { userDto.User.Id }, userDto.PostIds);
            return res1 && res2 && res3;
        }

        public async Task<bool> AddInfo(UserInfoDto userDto)
        {
            userDto.User.BuildPassword();
            var res1 = await _repository.InsertReturnSnowflakeIdAsync(userDto.User);
            var res2 = await GiveUserSetRole(new List<long> { res1 }, userDto.RoleIds);
            var res3 = await GiveUserSetPost(new List<long> { res1 }, userDto.PostIds);
            return !0.Equals(res1) && res2 && res3;
        }

        public async Task<bool> RestPassword(long userId, string password)
        {
            var user = new UserEntity();
            user.Id = userId;
            user.Password = password;
            user.BuildPassword();

            return await _repository.UpdateIgnoreNullAsync(user);


        }


        public async Task<bool> UpdatePassword(UpdatePasswordDto dto, long userId)
        {
            var user = await _repository.GetByIdAsync(userId);

            if (dto.OldPassword.Equals(dto.NewPassword))
            {
                return false;
            }
            if (!user.JudgePassword(dto.OldPassword))
            {
                return false;
            }
            var newUser = new UserEntity();
            newUser.Password = dto.NewPassword;
            newUser.Id = userId;
            newUser.BuildPassword();
            return await _repository.UpdateIgnoreNullAsync(newUser);
        }


        public async Task<bool> UpdateProfile(UserInfoDto userDto)
        {
            userDto.User.Salt = null;
            userDto.User.Password = null;
            userDto.User.DeptId = null;
            return await _repository.UpdateIgnoreNullAsync(userDto.User);
        }
    }
}
