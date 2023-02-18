using AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.ERP.Entitys;

namespace Yi.Framework.DtoModel.ERP.Supplier.MapperConfig
{
    public class SupplierProfile:Profile
    {
        public SupplierProfile()
        {
            CreateMap<SupplierCreateUpdateInput, SupplierEntity>();
            CreateMap<SupplierEntity, SupplierGetListOutput>();

        }
    }
}
