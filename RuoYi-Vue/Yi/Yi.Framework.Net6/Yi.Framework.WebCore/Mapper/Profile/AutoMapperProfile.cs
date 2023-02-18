using AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.DtoModel.Base.Vo;
using Yi.Framework.Model.BBS.Entitys;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.WebCore.Mapper
{
    public class AutoMapperProfile : Profile
    {
        // 添加你的实体映射关系. 
        public AutoMapperProfile()
        {
            CreateMap<ArticleEntity, ArticleVo>();
            CreateMap<UserEntity, UserVo>();
            CreateMap<CommentEntity, CommentVo>();
        }
    }


}
