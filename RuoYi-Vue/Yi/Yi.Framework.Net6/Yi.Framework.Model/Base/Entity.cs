using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base
{
    public class Entity<Key> : IEntity<Key>
    {
        public Key Id { get; set; }=default(Key)!;

        public object[] GetKeys()
        {
            throw new NotImplementedException();
        }
    }
}
