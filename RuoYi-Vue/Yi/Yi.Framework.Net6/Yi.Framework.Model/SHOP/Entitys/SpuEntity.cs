using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.SHOP.Entitys
{
    /// <summary>
    /// Spu表
    ///</summary>
    [SugarTable("Spu")]
    public partial class SpuEntity : IBaseModelEntity
    {
        public SpuEntity()
        {
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        /// 商品分类Id 
        ///</summary>
        [SugarColumn(ColumnName = "CategoryId")]
        public long? CategoryId { get; set; }
        /// <summary>
        /// 商品名称 
        ///</summary>
        [SugarColumn(ColumnName = "SpuName")]
        public string? SpuName { get; set; }
        /// <summary>
        /// 商品详情 
        ///</summary>
        [SugarColumn(ColumnName = "Details")]
        public string? Details { get; set; }
        /// <summary>
        /// 商品价格 
        ///</summary>
        [SugarColumn(ColumnName = "Price")]
        public string? Price { get; set; }

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
        public string? Remark { get; set; } /// <summary>
                                            /// 规格Spu完整信息 
                                            ///</summary>
        [SugarColumn(ColumnName = "SpecsAllInfo", IsJson = true)]
        public List<SpecsSpuAllInfoModel>? SpecsSpuAllInfo { get; set; }
        /// <summary>
        /// 规格Spu信息 
        ///</summary>
        [SugarColumn(ColumnName = "SpecsInfo", IsJson = true)]
        public List<SpecsSpuInfoModel>? SpecsSpuInfo { get; set; }

        [Navigate(NavigateType.OneToMany, nameof(SkuEntity.SpuId))]
        public List<SkuEntity>? Skus { get; set; }
    }

    public class SpecsSpuAllInfoModel
    {
        public string? SpecsGroupName { get; set; }
        public List<string>? SpecsNames { get; set; }
    }
    public class SpecsSpuInfoModel
    {
        public long? SpecsGroupId { get; set; }
        public List<long>? SpecsIds { get; set; }
    }
}
