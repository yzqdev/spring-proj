using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.Model.RABC.SeedData
{
    public class SeedFactory
    {
        public static List<UserEntity> GetUserSeed()
        {
            return new UserSeed().GetSeed();
        }
        public static List<RoleEntity> GetRoleSeed()
        {
            return new RoleSeed().GetSeed();
        }
        public static List<MenuEntity> GetMenuSeed()
        {
            return new MenuSeed().GetSeed();
        }
        public static List<DictionaryEntity> GetDictionarySeed()
        {
            return new DictionarySeed().GetSeed();
        }
        public static List<PostEntity> GetPostSeed()
        {
            return new PostSeed().GetSeed();
        }

        public static List<DictionaryInfoEntity> GetDictionaryInfoSeed()
        {
            return new DictionaryInfoSeed().GetSeed();
        }

        public static List<DeptEntity> GetDeptSeed()
        {
            return new DeptSeed().GetSeed();
        }

        public static List<FileEntity> GetFileSeed()
        {
            return new FileSeed().GetSeed();
        }
        public static List<UserRoleEntity> GetUserRoleSeed(List<UserEntity> users, List<RoleEntity> roles)
        {
            List<UserRoleEntity> userRoleEntities = new();
            foreach (var u in users)
            {
                foreach (var r in roles)
                {
                    userRoleEntities.Add(new UserRoleEntity() { Id = SnowFlakeSingle.Instance.NextId(), UserId = u.Id, RoleId = r.Id, IsDeleted = false });
                }
            }
            return userRoleEntities;
        }

        public static List<RoleMenuEntity> GetRoleMenuSeed(List<RoleEntity> roles, List<MenuEntity> menus)
        {
            List<RoleMenuEntity> roleMenuEntities = new();
            foreach (var r in roles)
            {
                foreach (var m in menus)
                {
                    roleMenuEntities.Add(new RoleMenuEntity() { Id = SnowFlakeSingle.Instance.NextId(), RoleId = r.Id, MenuId = m.Id, IsDeleted = false });
                }
            }
            return roleMenuEntities;
        }
    }
}
