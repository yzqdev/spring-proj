using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base
{
    public interface IBaseModelEntity
    {
        public long Id { get; set; }

        public long? CreateUser { get; set; }

        public long? ModifyUser { get; set; }

        public DateTime? CreateTime { get; set; }

        public DateTime? ModifyTime { get; set; }
        public bool? IsDeleted { get; set; }
    }
}
