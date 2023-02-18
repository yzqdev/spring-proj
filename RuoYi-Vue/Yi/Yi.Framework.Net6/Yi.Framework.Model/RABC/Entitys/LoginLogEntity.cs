using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 登入日志
    ///</summary>
    [SugarTable("LoginLog")]
    public partial class LoginLogEntity : IBaseModelEntity
    {
        public LoginLogEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 登录用户 
        ///</summary>
        [SugarColumn(ColumnName = "LoginUser")]
        public string? LoginUser { get; set; }
        /// <summary>
        /// 登录地点 
        ///</summary>
        [SugarColumn(ColumnName = "LoginLocation")]
        public string? LoginLocation { get; set; }
        /// <summary>
        /// 登录Ip 
        ///</summary>
        [SugarColumn(ColumnName = "LoginIp")]
        public string? LoginIp { get; set; }
        /// <summary>
        /// 浏览器 
        ///</summary>
        [SugarColumn(ColumnName = "Browser")]
        public string? Browser { get; set; }
        /// <summary>
        /// 操作系统 
        ///</summary>
        [SugarColumn(ColumnName = "Os")]
        public string? Os { get; set; }
        /// <summary>
        /// 登录信息 
        ///</summary>
        [SugarColumn(ColumnName = "LogMsg")]
        public string? LogMsg { get; set; }
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
