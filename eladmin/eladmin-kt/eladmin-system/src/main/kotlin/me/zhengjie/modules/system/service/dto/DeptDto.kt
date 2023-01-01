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
package me.zhengjie.modules.system.service.dto

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseDTO
import java.io.Serializable
import java.util.*

/**
 * @author Zheng Jie
 * @date 2019-03-25
 */
@Getter
@Setter
class DeptDto : BaseDTO(), Serializable {
    private val id: Long? = null
    val label: String? = null
    private val enabled: Boolean? = null
    private val deptSort: Int? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private val children: List<DeptDto>? = null
    private val pid: Long? = null
    private val subCount: Int? = null
    val hasChildren: Boolean
        get() = subCount!! > 0
    val leaf: Boolean
        get() = subCount!! <= 0

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val deptDto = o as DeptDto
        return id == deptDto.id && label == deptDto.label
    }

    override fun hashCode(): Int {
        return Objects.hash(id, label)
    }
}