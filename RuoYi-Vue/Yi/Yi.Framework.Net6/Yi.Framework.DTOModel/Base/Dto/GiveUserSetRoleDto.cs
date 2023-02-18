using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.DtoModel.Base.Dto
{
    public class GiveUserSetRoleDto
    {
        public List<long>? UserIds { get; set; }
        public List<long>? RoleIds { get; set; }
    }
}
