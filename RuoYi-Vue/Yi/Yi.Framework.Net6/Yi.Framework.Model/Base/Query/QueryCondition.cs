using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Model.Base.Query
{
    public class QueryPageCondition
    {
        public int Index { get; set; }
        public int Size { get; set; }

        public List<QueryParameter> Parameters { get; set; } = new List<QueryParameter>();
        public List<string> OrderBys { get; set; } = new List<string>();

        public bool IsAsc { get; set; } = false;
    }

    public class QueryCondition
    {
        public List<QueryParameter> Parameters { get; set; } = new List<QueryParameter>();
        public List<string> OrderBys { get; set; } = new List<string>();

        public bool IsAsc { get; set; } = false;

    }
}
