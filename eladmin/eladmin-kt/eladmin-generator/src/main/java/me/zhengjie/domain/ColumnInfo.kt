/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.domain

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import me.zhengjie.utils.GenUtil
import java.io.Serializable
import javax.persistence.*

/**
 * 列的数据信息
 * @author Zheng Jie
 * @date 2019-01-02
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "code_column_config")
class ColumnInfo(
    @field:Schema(name = "表名") private val tableName: String,
    @field:Schema(
        name = "数据库字段名称"
    ) private val columnName: String,
    @field:Schema(name = "是否必填") private val notNull: Boolean,
    @field:Schema(
        name = "数据库字段类型"
    ) private val columnType: String,
    remark: String,
    @field:Schema(name = "数据库字段键类型") private val keyType: String,
    @field:Schema(
        name = "字段额外的参数"
    ) private val extra: String
) : Serializable {
    @Id
    @Column(name = "column_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Schema(name = "数据库字段描述")
    private val remark: String

    @Schema(name = "是否在列表显示")
    private val listShow: Boolean

    @Schema(name = "是否表单显示")
    private val formShow: Boolean

    @Schema(name = "表单类型")
    private val formType: String? = null

    @Schema(name = "查询 1:模糊 2：精确")
    private val queryType: String? = null

    @Schema(name = "字典名称")
    private val dictName: String? = null

    @Schema(name = "日期注解")
    private val dateAnnotation: String? = null

    init {
        if (GenUtil.PK.equals(keyType, ignoreCase = true) && GenUtil.EXTRA.equals(extra, ignoreCase = true)) {
            notNull = false
        }
        this.remark = remark
        listShow = true
        formShow = true
    }
}