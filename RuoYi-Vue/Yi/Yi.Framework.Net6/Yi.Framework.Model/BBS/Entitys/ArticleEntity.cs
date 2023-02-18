using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;
using Yi.Framework.Model.RABC.Entitys;

namespace Yi.Framework.Model.BBS.Entitys
{
    /// <summary>
    /// 文章表
    ///</summary>
    [SugarTable("Article")]
    public partial class ArticleEntity : IBaseModelEntity
    {
        public ArticleEntity()
        {
            CreateTime = DateTime.Now;
            AgreeNum = 0;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 文章标题 
        ///</summary>
        [SugarColumn(ColumnName = "Title")]
        public string? Title { get; set; }
        /// <summary>
        /// 文章内容 
        ///</summary>
        [SugarColumn(ColumnName = "Content")]
        public string? Content { get; set; }
        /// <summary>
        /// 用户id 
        ///</summary>
        [SugarColumn(ColumnName = "UserId")]
        public long? UserId { get; set; }
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
        /// 修改者 
        ///</summary>
        [SugarColumn(ColumnName = "ModifyUser")]
        public long? ModifyUser { get; set; }
        /// <summary>
        /// 修改时间 
        ///</summary>
        [SugarColumn(ColumnName = "ModifyTime")]
        public DateTime? ModifyTime { get; set; }
        /// <summary>
        /// 是否删除 
        ///</summary>
        [SugarColumn(ColumnName = "IsDeleted")]
        public bool? IsDeleted { get; set; }
        /// <summary>
        /// 租户Id 
        ///</summary>
        [SugarColumn(ColumnName = "TenantId")]
        public long? TenantId { get; set; }
        /// <summary>
        /// 排序字段 
        ///</summary>
        [SugarColumn(ColumnName = "OrderNum")]
        public int? OrderNum { get; set; }
        /// <summary>
        /// 描述 
        ///</summary>
        [SugarColumn(ColumnName = "Remark")]
        public string? Remark { get; set; }
        /// <summary>
        /// 图片列表 
        ///</summary>
        [SugarColumn(ColumnName = "Images", IsJson = true)]
        public List<string>? Images { get; set; }
        /// <summary>
        /// 点赞数量 
        ///</summary>
        [SugarColumn(ColumnName = "AgreeNum")]
        public int AgreeNum { get; set; }

        [Navigate(NavigateType.OneToOne, nameof(UserId))]
        public UserEntity? User { get; set; }
    }
}
