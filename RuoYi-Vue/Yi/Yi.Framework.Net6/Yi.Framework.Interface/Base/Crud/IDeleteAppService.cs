using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Interface.Base.Crud
{
    public interface IDeleteAppService<in TKey> : IApplicationService
    {
        Task DeleteAsync(IEnumerable<TKey> ids);
    }
}
