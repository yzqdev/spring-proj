using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 租户表
    ///</summary>
    [SugarTable("Tenant")]
    public partial class TenantEntity : IBaseModelEntity
    {
        public TenantEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "TenantName")]
        public string? TenantName { get; set; }
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

        //    //需要在用户表中关联好该租户信息，一个用户关联一个租户
        //    //不同租户下，用户可以相同
        //    //用户登录后，token中可包含租户id，同时缓存一份用户信息(包含租户信息)
        //    [Tenant("0")]
        //    //当然，像用户、角色、菜单、租户为共享库了
        //    [SugarTable("Tenant")]
        //    public class TenantEntity
        //    {
        //        /// <summary>
        //        /// 主键唯一标识
        //        /// </summary>
        //        [SugarColumn(IsPrimaryKey = true)]
        //        public long Id { get; set; }

        //        /// <summary>
        //        /// 租户id
        //        /// </summary>
        //        public string? TenantId { get; set;  }

        //        /// <summary>
        //        /// 业务库连接字符串
        //        /// </summary>
        //        public string? Connection { get; set; }

        //        /// <summary>
        //        /// 业务库连接类型
        //        /// </summary>
        //        public string? DbType { get; set; }
    }
}
