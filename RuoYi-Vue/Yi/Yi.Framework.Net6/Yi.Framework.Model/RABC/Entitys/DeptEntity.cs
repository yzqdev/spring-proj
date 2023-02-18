using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 部门表
    ///</summary>
    [SugarTable("Dept")]
    public partial class DeptEntity : IBaseModelEntity
    {
        public DeptEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 部门名称 
        ///</summary>
        [SugarColumn(ColumnName = "DeptName")]
        public string? DeptName { get; set; }
        /// <summary>
        /// 部门编码 
        ///</summary>
        [SugarColumn(ColumnName = "DeptCode")]
        public string? DeptCode { get; set; }
        /// <summary>
        /// 负责人 
        ///</summary>
        [SugarColumn(ColumnName = "Leader")]
        public string? Leader { get; set; }
        /// <summary>
        /// 父级id 
        ///</summary>
        [SugarColumn(ColumnName = "ParentId")]
        public long? ParentId { get; set; }
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
