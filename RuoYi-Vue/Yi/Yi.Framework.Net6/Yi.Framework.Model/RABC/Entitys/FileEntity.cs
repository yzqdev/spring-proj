using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 文件表
    ///</summary>
    [SugarTable("File")]
    public partial class FileEntity : IBaseModelEntity
    {
        public FileEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 文件类型 
        ///</summary>
        [SugarColumn(ColumnName = "FileType")]
        public string? FileType { get; set; }
        /// <summary>
        /// 文件大小 
        ///</summary>
        [SugarColumn(ColumnName = "FileSize")]
        public decimal? FileSize { get; set; }
        /// <summary>
        /// 文件名 
        ///</summary>
        [SugarColumn(ColumnName = "FileName")]
        public string? FileName { get; set; }
        /// <summary>
        /// 文件路径 
        ///</summary>
        [SugarColumn(ColumnName = "FilePath")]
        public string? FilePath { get; set; }
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
    }
}
