using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base
{
    [Serializable]
    public abstract class EntityDto<TKey> : EntityDto, IEntityDto<TKey>, IEntityDto
    {
        //
        // 摘要:
        //     Id of the entity.
        public TKey? Id { get; set; }

        public override string ToString()
        {
            return $"[DTO: {GetType().Name}] Id = {Id}";
        }
    }

    [Serializable]
    public abstract class EntityDto : IEntityDto
    {
        public override string ToString()
        {
            return "[DTO: " + GetType().Name + "]";
        }
    }
}
