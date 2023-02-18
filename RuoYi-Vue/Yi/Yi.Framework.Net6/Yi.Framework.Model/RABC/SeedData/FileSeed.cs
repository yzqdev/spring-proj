using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.Model.RABC.SeedData
{
    public class FileSeed : AbstractSeed<FileEntity>
    {
        public override List<FileEntity> GetSeed()
        {
            FileEntity file1 = new FileEntity()
            {
                Id = 0,
                FilePath = "Image",
                FileType = "",
                IsDeleted = false,
                FileName = "0.jpg"
            };
            Entitys.Add(file1);
            return Entitys;
        }
    }
}
