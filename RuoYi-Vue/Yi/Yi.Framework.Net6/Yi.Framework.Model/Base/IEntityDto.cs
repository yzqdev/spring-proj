using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base
{
    public interface IEntityDto
    {

    }

    public interface IEntityDto<TKey> : IEntityDto
    {
        TKey? Id { get; set; }
    }
}
