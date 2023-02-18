using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.DtoModel.Base.Vo
{
    /// <summary>
    /// 前端只需要这些数据即可
    /// </summary>
    public class UserVo
    {
        public string? Name { get; set; }

        public int? Age { get; set; }

        public string? UserName { get; set; }

        public string? Icon { get; set; }

        public string? Nick { get; set; }

        //public string Email { get; set; }

        //public string Ip { get; set; }


        //public string Address { get; set; }

        //public string Phone { get; set; }

        public string? Introduction { get; set; }

        public int? Sex { get; set; }
    }
}
