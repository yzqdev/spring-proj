using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.DtoModel.Base.Dto
{
    public class UserInfoDto
    {
        public UserEntity? User { get; set; }
        public List<long>? RoleIds { get; set; }
        public List<long>? PostIds { get; set; }
        public long? DeptId { get; set; }
    }
}
