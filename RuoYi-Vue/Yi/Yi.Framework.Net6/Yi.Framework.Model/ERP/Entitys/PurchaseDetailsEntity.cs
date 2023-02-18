using Newtonsoft.Json;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.ERP.Entitys
{
    /// <summary>
    /// 采购订单子表
    /// </summary>
    [SugarTable("PurchaseDetail")]
    public class PurchaseDetailsEntity : IEntity<long>, IMultiTenant
    {
        /// <summary>
        /// 主键
        /// </summary>
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(IsPrimaryKey = true)]
        public long Id { get; set; } 

        /// <summary>
        /// 租户id
        /// </summary>
        public Guid? TenantId { get; set; }

        /// <summary>
        /// 采购单id
        /// </summary>
        public long PurchaseId { get; set; }

        /// <summary>
        /// 物料id
        /// </summary>
        public long MaterialId { get; set; } 


        /// <summary>
        /// 物料名称
        /// </summary>
        public string MaterialName { get; set; }=string.Empty;

        /// <summary>
        /// 物料单位
        /// </summary>
        public string MaterialUnit { get; set; }=string.Empty ;


        /// <summary>
        /// 单价
        /// </summary>
        public float UnitPrice { get; set; } 
        /// <summary>
        /// 总数量
        /// </summary>
        public long TotalNumber { get; set; } 
        /// <summary>
        /// 已完成数量
        /// </summary>
        public long CompleteNumber { get; set; }
        /// <summary>
        /// 备注
        /// </summary>
        public string? Remarks { get; set; }
    }
}
