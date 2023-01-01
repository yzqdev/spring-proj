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

import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseDTO
import java.io.Serializable
import java.util.*

/**
 * @author Zheng Jie
 * @date 2018-12-17
 */
@Getter
@Setter
class MenuDto : BaseDTO(), Serializable {
    private val id: Long? = null
    private val children: List<MenuDto>? = null
    private val type: Int? = null
    private val permission: String? = null
    val label: String? = null
    private val menuSort: Int? = null
    private val path: String? = null
    private val component: String? = null
    private val pid: Long? = null
    private val subCount: Int? = null
    private val iFrame: Boolean? = null
    private val cache: Boolean? = null
    private val hidden: Boolean? = null
    private val componentName: String? = null
    private val icon: String? = null
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
        val menuDto = o as MenuDto
        return id == menuDto.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}