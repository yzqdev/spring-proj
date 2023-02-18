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
    /// 单位定义
    /// </summary>
    [SugarTable("Unit")]
    public class UnitEntity : IEntity<long>, IMultiTenant
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
        /// 单位编码
        /// </summary>
        public string Code { get; set; }=string.Empty;

        /// <summary>
        /// 单位名称
        /// </summary>
        public string Name { get; set; }=string.Empty;

        /// <summary>
        /// 备注
        /// </summary>
        public string? Remarks { get; set; }
    }
}
