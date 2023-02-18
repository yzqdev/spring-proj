using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.DtoModel.Base.Dto
{
    public class UpdatePasswordDto
    {
        public string NewPassword { get; set; }=string.Empty;
        public string OldPassword { get; set; } = string.Empty;
    }
}
