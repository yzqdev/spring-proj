using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.DtoModel.Base.Dto
{
    public class RoleInfoDto
    {
        public RoleEntity Role { get; set; }=new ();
        public List<long> DeptIds { get; set; } = new ();
        public List<long> MenuIds { get; set; } = new ();
    }
}
