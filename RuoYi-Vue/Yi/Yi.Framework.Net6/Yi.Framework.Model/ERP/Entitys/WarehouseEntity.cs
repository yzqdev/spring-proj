using Newtonsoft.Json;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Enum;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.ERP.Entitys
{
    /// <summary>
    /// 仓库定义
    /// </summary>
    [SugarTable("Warehouse")]
    public class WarehouseEntity : IEntity<long>, IMultiTenant
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
        /// 仓库编码
        /// </summary>
        public string Code { get; set; }=string.Empty;

        /// <summary>
        /// 仓库名称
        /// </summary>
        public string Name { get; set; }=string.Empty ;

        /// <summary>
        /// 备注
        /// </summary>
        public string? Remarks { get; set; }

        /// <summary>
        /// 状态
        /// </summary>
        public StateEnum State { get; set; } = StateEnum.Normal;
    }
}
