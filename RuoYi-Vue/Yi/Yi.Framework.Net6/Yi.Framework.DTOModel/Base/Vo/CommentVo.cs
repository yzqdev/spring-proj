using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.DtoModel.Base.Vo
{
    public class CommentVo
    {
        public long Id { get; set; }

        /// <summary>
        /// 评论内容 
        ///</summary>
        public string Content { get; set; }=string.Empty;
        /// <summary>
        /// 点赞数 
        ///</summary>
        public int? AgreeNum { get; set; }

        /// <summary>
        /// 创建时间 
        ///</summary>
        public DateTime? CreateTime { get; set; }
        /// <summary>
        /// 是否删除 
        ///</summary>
        public bool? IsDeleted { get; set; }
        /// <summary>
        /// 租户Id 
        ///</summary>
        public long? TenantId { get; set; }
        /// <summary>
        /// 排序字段 
        ///</summary>

        public int? OrderNum { get; set; }
        /// <summary>
        /// 描述 
        ///</summary>

        public string? Remark { get; set; }
        /// <summary>
        /// 子评论数 
        ///</summary>

        public int? CommentNum { get; set; }

        /// <summary>
        /// 被回复的用户信息
        ///</summary>
        public UserVo? UserInfo { get; set; }

        /// <summary>
        /// 创建评论的用户信息 
        ///</summary>
        public UserVo? CreateUserInfo { get; set; }
    }
}
