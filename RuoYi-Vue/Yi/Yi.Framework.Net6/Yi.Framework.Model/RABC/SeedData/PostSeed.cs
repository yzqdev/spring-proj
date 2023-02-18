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
    public class PostSeed : AbstractSeed<PostEntity>
    {
        public override List<PostEntity> GetSeed()
        {
            PostEntity Post1 = new PostEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                PostName = "董事长",
                PostCode = "ceo",
                OrderNum = 100,
                IsDeleted = false
            };
            Entitys.Add(Post1);

            PostEntity Post2 = new PostEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                PostName = "项目经理",
                PostCode = "se",
                OrderNum = 100,
                IsDeleted = false
            };
            Entitys.Add(Post2);

            PostEntity Post3 = new PostEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                PostName = "人力资源",
                PostCode = "hr",
                OrderNum = 100,
                IsDeleted = false
            };
            Entitys.Add(Post3);

            PostEntity Post4 = new PostEntity()
            {
                Id = SnowFlakeSingle.Instance.NextId(),
                PostName = "普通员工",
                PostCode = "user",
                OrderNum = 100,
                IsDeleted = false
            };
            Entitys.Add(Post4);
            return Entitys;
        }
    }
}
