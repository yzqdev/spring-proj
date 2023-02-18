using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 操作日志表
    ///</summary>
    [SugarTable("OperationLog")]
    public partial class OperationLogEntity : IBaseModelEntity
    {
        public OperationLogEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 操作模块 
        ///</summary>
        [SugarColumn(ColumnName = "Title")]
        public string? Title { get; set; }
        /// <summary>
        /// 操作类型 
        ///</summary>
        [SugarColumn(ColumnName = "OperType")]
        public int? OperType { get; set; }
        /// <summary>
        /// 请求方法 
        ///</summary>
        [SugarColumn(ColumnName = "RequestMethod")]
        public string? RequestMethod { get; set; }
        /// <summary>
        /// 操作人员 
        ///</summary>
        [SugarColumn(ColumnName = "OperUser")]
        public string? OperUser { get; set; }
        /// <summary>
        /// 操作Ip 
        ///</summary>
        [SugarColumn(ColumnName = "OperIp")]
        public string? OperIp { get; set; }
        /// <summary>
        /// 操作地点 
        ///</summary>
        [SugarColumn(ColumnName = "OperLocation")]
        public string? OperLocation { get; set; }
        /// <summary>
        /// 操作方法 
        ///</summary>
        [SugarColumn(ColumnName = "Method")]
        public string? Method { get; set; }
        /// <summary>
        /// 请求参数 
        ///</summary>
        [SugarColumn(ColumnName = "RequestParam")]
        public string? RequestParam { get; set; }
        /// <summary>
        /// 请求结果 
        ///</summary>
        [SugarColumn(ColumnName = "RequestResult")]
        public string? RequestResult { get; set; }
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
        /// 是否删除 
        ///</summary>
        [SugarColumn(ColumnName = "IsDeleted")]
        public bool? IsDeleted { get; set; }
    }
}
