using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Enum;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.Model.RABC.SeedData
{
    public class RoleSeed : AbstractSeed<RoleEntity>
    {
        public override List<RoleEntity> GetSeed()
        {
            RoleEntity role1 = new RoleEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                RoleName = "管理员",
                RoleCode = "admin",
                DataScope = DataScopeEnum.ALL.GetHashCode(),
                OrderNum = 999,
                Remark = "管理员",
                IsDeleted = false
            };
            Entitys.Add(role1);

            RoleEntity role2 = new RoleEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                RoleName = "测试角色",
                RoleCode = "test",
                DataScope = DataScopeEnum.ALL.GetHashCode(),
                OrderNum = 1,
                Remark = "测试用的角色",
                IsDeleted = false
            };
            Entitys.Add(role2);

            return Entitys;
        }
    }
}
