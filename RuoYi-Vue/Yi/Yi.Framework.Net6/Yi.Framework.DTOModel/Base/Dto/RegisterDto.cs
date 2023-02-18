using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.DtoModel.Base.Dto
{
    public class RegisterDto
    {
        public string UserName { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;

        public string Name { get; set; } = string.Empty;
    }
}
