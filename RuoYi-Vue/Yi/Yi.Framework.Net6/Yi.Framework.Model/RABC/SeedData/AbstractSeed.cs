using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.RABC.SeedData
{
    public abstract class AbstractSeed<T>
    {
        protected List<T> Entitys { get; set; } = new List<T>();
        public virtual List<T> GetSeed()
        {
            return Entitys;
        }
    }
}
