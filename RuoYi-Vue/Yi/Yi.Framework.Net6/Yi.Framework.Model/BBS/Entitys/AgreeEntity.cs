using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;

namespace Yi.Framework.Model.BBS.Entitys
{
    /// <summary>
    /// 点赞表
    ///</summary>
    [SugarTable("Agree")]
    public partial class AgreeEntity
    {
        public AgreeEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 用户id 
        ///</summary>
        [SugarColumn(ColumnName = "UserId")]
        public long? UserId { get; set; }
        /// <summary>
        /// 文章或评论id 
        ///</summary>
        [SugarColumn(ColumnName = "ArticleOrCommentId")]
        public long? ArticleOrCommentId { get; set; }
        /// <summary>
        /// 创建者 
        ///</summary>
        [SugarColumn(ColumnName = "CreateUser")]
        public long? CreateUser { get; set; }
        /// <summary>
        /// 创建时间 
        ///</summary>
        [SugarColumn(ColumnName = "CreateTime")]
        public DateTime? CreateTime { get; set; }
        /// <summary>
        /// 租户Id 
        ///</summary>
        [SugarColumn(ColumnName = "TenantId")]
        public long? TenantId { get; set; }
    }
}
