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
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 * 代码生成配置
 * @author Zheng Jie
 * @date 2019-01-03
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "code_gen_config")
class GenConfig(@field:Schema(name = "表名") private val tableName: @NotBlank String?) : Serializable {
    @Id
    @Column(name = "config_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Schema(name = "接口名称")
    private val apiAlias: String? = null

    @Schema(name = "包路径")
    private val pack: @NotBlank String? = null

    @Schema(name = "模块名")
    private val moduleName: @NotBlank String? = null

    @Schema(name = "前端文件路径")
    private val path: @NotBlank String? = null

    @Schema(name = "前端文件路径")
    private val apiPath: String? = null

    @Schema(name = "作者")
    private val author: String? = null

    @Schema(name = "表前缀")
    private val prefix: String? = null

    @Schema(name = "是否覆盖")
    private val cover = false
}