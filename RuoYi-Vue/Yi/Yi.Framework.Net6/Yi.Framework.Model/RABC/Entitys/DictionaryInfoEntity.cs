using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 字典信息表
    ///</summary>
    [SugarTable("DictionaryInfo")]
    public partial class DictionaryInfoEntity : IBaseModelEntity
    {
        public DictionaryInfoEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 字典类型 
        ///</summary>
        [SugarColumn(ColumnName = "DictType")]
        public string? DictType { get; set; }
        /// <summary>
        /// 字典标签 
        ///</summary>
        [SugarColumn(ColumnName = "DictLabel")]
        public string? DictLabel { get; set; }
        /// <summary>
        /// 字典值 
        ///</summary>
        [SugarColumn(ColumnName = "DictValue")]
        public string? DictValue { get; set; }
        /// <summary>
        /// 是否为该类型的默认值 
        ///</summary>
        [SugarColumn(ColumnName = "IsDefault")]
        public bool? IsDefault { get; set; }
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
        /// tag类型 
        ///</summary>
        [SugarColumn(ColumnName = "ListClass")]
        public string? ListClass { get; set; }
        /// <summary>
        /// tagClass 
        ///</summary>
        [SugarColumn(ColumnName = "CssClass")]
        public string? CssClass { get; set; }
    }
}
