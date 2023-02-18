using AutoMapper;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Material;
using Yi.Framework.Interface.ERP;
using Yi.Framework.Model.ERP.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service.Base.Crud;

namespace Yi.Framework.Service.ERP
{
    public class MaterialService : CrudAppService<MaterialEntity, MaterialGetListOutput, long, MaterialCreateUpdateInput>, IMaterialService
    {
        public async Task<PageModel<List<MaterialGetListOutput>>> PageListAsync(MaterialCreateUpdateInput input, PageParModel page)
        {
            RefAsync<int> totalNumber = 0;
            var data = await Repository._DbQueryable
                .WhereIF(input.Code is not null,u=>u.Code.Contains(input.Code))
                .WhereIF(input.Name is not null, u => u.Name.Contains(input.Name))
                .ToPageListAsync(page.PageNum, page.PageSize, totalNumber);
            return new PageModel<List<MaterialGetListOutput>> { Total = totalNumber.Value, Data = await MapToGetListOutputDtosAsync(data) };
        }
    }
}
