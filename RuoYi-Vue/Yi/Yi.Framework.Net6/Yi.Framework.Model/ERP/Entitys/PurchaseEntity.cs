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
    /// 采购订单
    /// </summary>
    [SugarTable("Purchase")]
    public class PurchaseEntity : IEntity<long>, IMultiTenant
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
        /// 采购单号
        /// </summary>
        public string Code { get; set; }=string.Empty;

        /// <summary>
        /// 需求时间 
        /// </summary>
        public DateTime? NeedTime { get; set; }

        /// <summary>
        /// 采购员
        /// </summary>
        public string? Buyer { get; set; }

        /// <summary>
        /// 总共金额
        /// </summary>
        public float TotalMoney { get; set; } 

        /// <summary>
        /// 已支付金额
        /// </summary>
        public float PaidMoney { get; set; }

        /// <summary>
        /// 采购状态
        /// </summary>
        public PurchaseStateEnum PurchaseState { get; set; } = PurchaseStateEnum.Build;

    }

    public enum PurchaseStateEnum
    {
        Build=0,//新建
        Run=1,//进行中
        Complete=2,//已完成
        End=3//已结束
    }
}
