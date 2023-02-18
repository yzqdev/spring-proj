using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using SqlSugar;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Models;
using Yi.Framework.Model.Base;

namespace Yi.Framework.Model.RABC.Entitys
{
    /// <summary>
    /// 菜单表
    ///</summary>
    [SugarTable("Menu")]
    public partial class MenuEntity : IBaseModelEntity
    {
        public MenuEntity()
        {
            IsCache = false;
            CreateTime = DateTime.Now;
        }
        [JsonConverter(typeof(ValueToStringConverter))]
        [SugarColumn(ColumnName = "Id", IsPrimaryKey = true)]
        public long Id { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "MenuName")]
        public string? MenuName { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "MenuType")]
        public int? MenuType { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "PermissionCode")]
        public string? PermissionCode { get; set; }
        /// <summary>
        ///  
        ///</summary>
        [SugarColumn(ColumnName = "ParentId")]
        public long? ParentId { get; set; }
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
        /// 菜单图标 
        ///</summary>
        [SugarColumn(ColumnName = "MenuIcon")]
        public string? MenuIcon { get; set; }
        /// <summary>
        /// 菜单组件路由 
        ///</summary>
        [SugarColumn(ColumnName = "Router")]
        public string? Router { get; set; }
        /// <summary>
        /// 是否为外部链接 
        ///</summary>
        [SugarColumn(ColumnName = "IsLink")]
        public bool? IsLink { get; set; }
        /// <summary>
        /// 是否缓存 
        ///</summary>
        [SugarColumn(ColumnName = "IsCache")]
        public bool? IsCache { get; set; }
        /// <summary>
        /// 是否显示 
        ///</summary>
        [SugarColumn(ColumnName = "IsShow")]
        public bool? IsShow { get; set; }
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
        /// 组件路径 
        ///</summary>
        [SugarColumn(ColumnName = "Component")]
        public string? Component { get; set; }
        /// <summary>
        /// 路由参数 
        ///</summary>
        [SugarColumn(ColumnName = "Query")]
        public string? Query { get; set; }

        [SugarColumn(IsIgnore = true)]
        public List<MenuEntity>? Children { get; set; }


        public static List<VueRouterModel> RouterBuild(List<MenuEntity> menus)
        {
            menus = menus.Where(m => m.MenuType != null && m.MenuType != MenuTypeEnum.Component.GetHashCode()).ToList();
            List<VueRouterModel> routers = new();
            foreach (var m in menus)
            {

                var r = new VueRouterModel();
                r.OrderNum = m.OrderNum ?? 0;
                var routerName = m.Router?.Split("/").LastOrDefault();
                r.Id = m.Id;
                r.ParentId = m.ParentId ?? -1;

                //开头大写
                r.Name = routerName?.First().ToString().ToUpper() + routerName?.Substring(1);
                r.Path = m.Router!;
                r.Hidden = !m.IsShow ?? false;


                if (m.MenuType == MenuTypeEnum.Catalogue.GetHashCode())
                {
                    r.Redirect = "noRedirect";
                    r.AlwaysShow = true;

                    //判断是否为最顶层的路由
                    if (0 == m.ParentId)
                    {
                        r.Component = "Layout";
                    }
                    else
                    {
                        r.Component = "ParentView";
                    }
                }
                if (m.MenuType == MenuTypeEnum.Menu.GetHashCode())
                {

                    r.Redirect = "noRedirect";
                    r.AlwaysShow = true;
                    r.Component = m.Component!;
                    r.AlwaysShow = false;
                }
                r.Meta = new Meta
                {
                    Title = m.MenuName!,
                    Icon = m.MenuIcon!,
                    NoCache = !m.IsCache ?? true
                };
                if (m.IsLink ?? false)
                {
                    r.Meta.link = m.Router!;
                    r.AlwaysShow = false;
                }

                routers.Add(r);
            }
            return Common.Helper.TreeHelper.SetTree(routers);
        }
    }
}
