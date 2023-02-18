using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Material;
using Yi.Framework.Interface.Base.Crud;

namespace Yi.Framework.Interface.ERP
{
    public interface IMaterialService : ICrudAppService<MaterialGetListOutput, long, MaterialCreateUpdateInput>
    {
        Task<PageModel<List<MaterialGetListOutput>>> PageListAsync(MaterialCreateUpdateInput input, PageParModel page);
    }
}
