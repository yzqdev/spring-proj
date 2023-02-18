// ==========================================================================
// This is auto-generated by gf cli tool. DO NOT EDIT THIS FILE MANUALLY.
// ==========================================================================

package internal

import (
	"github.com/gogf/gf/database/gdb"
	"github.com/gogf/gf/frame/g"
	"github.com/gogf/gf/frame/gmvc"
)

// SysModelInfoDao is the manager for logic model data accessing
// and custom defined data operations functions management.
type SysModelInfoDao struct {
	gmvc.M                      // M is the core and embedded struct that inherits all chaining operations from gdb.Model.
	DB      gdb.DB              // DB is the raw underlying database management object.
	Table   string              // Table is the table name of the DAO.
	Columns sysModelInfoColumns // Columns contains all the columns of Table that for convenient usage.
}

// SysModelInfoColumns defines and stores column names for table sys_model_info.
type sysModelInfoColumns struct {
	ModelId         string // 模型ID
	ModelCategoryId string // 模板分类id
	ModelName       string // 模型标识
	ModelTitle      string // 模型名称
	ModelPk         string // 主键字段
	ModelOrder      string // 默认排序字段
	ModelSort       string // 表单字段排序
	ModelList       string // 列表显示字段，为空显示全部
	ModelEdit       string // 可编辑字段，为空则除主键外均可以编辑
	ModelIndexes    string // 索引字段
	SearchList      string // 高级搜索的字段
	CreateTime      string // 创建时间
	UpdateTime      string // 更新时间
	ModelStatus     string // 状态
	ModelEngine     string // 数据库引擎
	CreateBy        string // 创建人
	UpdateBy        string // 修改人
}

var (
	// SysModelInfo is globally public accessible object for table sys_model_info operations.
	SysModelInfo = SysModelInfoDao{
		M:     g.DB("default").Model("sys_model_info").Safe(),
		DB:    g.DB("default"),
		Table: "sys_model_info",
		Columns: sysModelInfoColumns{
			ModelId:         "model_id",
			ModelCategoryId: "model_category_id",
			ModelName:       "model_name",
			ModelTitle:      "model_title",
			ModelPk:         "model_pk",
			ModelOrder:      "model_order",
			ModelSort:       "model_sort",
			ModelList:       "model_list",
			ModelEdit:       "model_edit",
			ModelIndexes:    "model_indexes",
			SearchList:      "search_list",
			CreateTime:      "create_time",
			UpdateTime:      "update_time",
			ModelStatus:     "model_status",
			ModelEngine:     "model_engine",
			CreateBy:        "create_by",
			UpdateBy:        "update_by",
		},
	}
)
