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
package me.zhengjie.modules.mnt.service.dto

import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseDTO
import java.io.Serializable
import java.util.*

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Getter
@Setter
class ServerDeployDto : BaseDTO(), Serializable {
    private val id: Long? = null
    private val name: String? = null
    private val ip: String? = null
    private val port: Int? = null
    private val account: String? = null
    private val password: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as ServerDeployDto
        return id == that.id && name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }
}