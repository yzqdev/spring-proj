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
    public class DeptSeed : AbstractSeed<DeptEntity>
    {
        public override List<DeptEntity> GetSeed()
        {

            DeptEntity chengziDept = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "橙子科技",
                DeptCode = "Yi",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = 0,
                Leader = "橙子",
                Remark = "如名所指"
            };
            Entitys.Add(chengziDept);


            DeptEntity shenzhenDept = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "深圳总公司",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = chengziDept.Id
            };
            Entitys.Add(shenzhenDept);


            DeptEntity jiangxiDept = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "江西总公司",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = chengziDept.Id
            };
            Entitys.Add(jiangxiDept);



            DeptEntity szDept1 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "研发部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = shenzhenDept.Id
            };
            Entitys.Add(szDept1);

            DeptEntity szDept2 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "市场部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = shenzhenDept.Id
            };
            Entitys.Add(szDept2);

            DeptEntity szDept3 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "测试部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = shenzhenDept.Id
            };
            Entitys.Add(szDept3);

            DeptEntity szDept4 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "财务部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = shenzhenDept.Id
            };
            Entitys.Add(szDept4);

            DeptEntity szDept5 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "运维部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = shenzhenDept.Id
            };
            Entitys.Add(szDept5);


            DeptEntity jxDept1 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "市场部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = jiangxiDept.Id
            };
            Entitys.Add(jxDept1);


            DeptEntity jxDept2 = new DeptEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                DeptName = "财务部门",
                OrderNum = 100,
                IsDeleted = false,
                ParentId = jiangxiDept.Id
            };
            Entitys.Add(jxDept2);


            return Entitys;
        }
    }
}
