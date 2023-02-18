using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 角色表
    ///</summary>
    [SugarTable("Role")]
    public partial class RoleEntity : IBaseModelEntity
    {
        public RoleEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "RoleName")]
        public string? RoleName { get; set; }
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
        /// 修改者 
        ///</summary>
        [SugarColumn(ColumnName = "ModifyUser")]
        public long? ModifyUser { get; set; }
        /// <summary>
        /// 角色编码 
        ///</summary>
        [SugarColumn(ColumnName = "RoleCode")]
        public string? RoleCode { get; set; }
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
        /// 角色数据范围 
        ///</summary>
        [SugarColumn(ColumnName = "DataScope")]
        public int? DataScope { get; set; }


        [Navigate(typeof(RoleMenuEntity), nameof(RoleMenuEntity.RoleId), nameof(RoleMenuEntity.MenuId))]
        public List<MenuEntity>? Menus { get; set; }

        [Navigate(typeof(RoleDeptEntity), nameof(RoleDeptEntity.RoleId), nameof(RoleDeptEntity.DeptId))]
        public List<DeptEntity>? Depts { get; set; }
    }
}
