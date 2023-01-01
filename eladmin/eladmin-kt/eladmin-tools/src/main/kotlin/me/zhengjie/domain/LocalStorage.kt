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

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import me.zhengjie.base.BaseEntity
import java.io.Serializable
import javax.persistence.*

/**
 * @author Zheng Jie
 * @date 2019-09-05
 */
@Getter
@Setter
@Entity
@Table(name = "tool_local_storage")
@NoArgsConstructor
class LocalStorage(
    @field:Schema(name = "真实文件名") private val realName: String, @field:Schema(
        name = "文件名"
    ) private val name: String, @field:Schema(name = "后缀") private val suffix: String, @field:Schema(
        name = "路径"
    ) private val path: String, @field:Schema(name = "类型") private val type: String, @field:Schema(
        name = "大小"
    ) private val size: String
) : BaseEntity(), Serializable {
    @Id
    @Column(name = "storage_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
    fun copy(source: LocalStorage?) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true))
    }
}