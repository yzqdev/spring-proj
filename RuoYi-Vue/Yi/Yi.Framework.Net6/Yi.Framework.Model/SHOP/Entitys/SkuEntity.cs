using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.SHOP.Entitys
{
    /// <summary>
    /// Sku表
    ///</summary>
    [SugarTable("Sku")]
    public partial class SkuEntity : IBaseModelEntity
    {
        public SkuEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// SpuId 
        ///</summary>
        [SugarColumn(ColumnName = "SpuId")]
        public long? SpuId { get; set; }
        /// <summary>
        /// 库存 
        ///</summary>
        [SugarColumn(ColumnName = "Stock")]
        public int? Stock { get; set; }
        /// <summary>
        /// 价格 
        ///</summary>
        [SugarColumn(ColumnName = "Price")]
        public int? Price { get; set; }

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

        /// <summary>
        /// 规格sku信息 
        ///</summary>
        [SugarColumn(ColumnName = "SpecsSkuInfo", IsJson = true)]
        public List<SpecsSkuInfoModel>? SpecsSkuInfo { get; set; }
        /// <summary>
        /// 规格sku完整信息 
        ///</summary>
        [SugarColumn(ColumnName = "SpecsSkuAllInfo", IsJson = true)]
        public List<SpecsSkuAllInfoModel>? SpecsSkuAllInfo { get; set; }
    }

    public class SpecsSkuAllInfoModel
    {
        public string? SpecsGroupName { get; set; }
        public string? SpecsName { get; set; }
    }
    public class SpecsSkuInfoModel
    {
        public long? SpecsGroupId { get; set; }
        public long? SpecsId { get; set; }
    }
}
