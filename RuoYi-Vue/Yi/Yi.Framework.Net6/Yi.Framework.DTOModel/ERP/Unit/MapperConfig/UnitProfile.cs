using AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.ERP.Entitys;

namespace Yi.Framework.DtoModel.ERP.Unit.MapperConfig
{
    public class SuppliERProfile:Profile
    {
        public SuppliERProfile()
        {
            CreateMap<UnitCreateUpdateInput, UnitEntity>();
            CreateMap<UnitEntity, UnitGetListOutput>();

        }
    }
}
