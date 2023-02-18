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
    /// 供应商
    /// </summary>
    [SugarTable("Supplier")]
    public class SupplierEntity : IEntity<long>, IMultiTenant
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
        /// 供应商编码
        /// </summary>
        public string Code { get; set; } = string.Empty;

        /// <summary>
        /// 供应商名称
        /// </summary>
        public string Name { get; set; }= string.Empty;

        /// <summary>
        /// 供应商地址
        /// </summary>
        public string? Address { get; set; }

        /// <summary>
        /// 电话
        /// </summary>
        public long? Phone { get; set; }
        /// <summary>
        /// 传真
        /// </summary>
        public string? Fax { get; set; }
        /// <summary>
        /// 邮箱
        /// </summary>
        public string? Email { get; set; }
    }
}
