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
    /// 物料定义
    /// </summary>
    [SugarTable("Material")]
    public class MaterialEntity : IEntity<long>, IMultiTenant
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
        /// 物料编码
        /// </summary>
        public string Code { get; set; }=string.Empty;

        /// <summary>
        /// 物料名称
        /// </summary>
        public string Name { get; set; }=string.Empty;

        /// <summary>
        /// 物料单位
        /// </summary>
        public string UnitName { get; set; }=string.Empty;

        /// <summary>
        /// 备注
        /// </summary>
        public string? Remarks { get; set; }
    }
}
