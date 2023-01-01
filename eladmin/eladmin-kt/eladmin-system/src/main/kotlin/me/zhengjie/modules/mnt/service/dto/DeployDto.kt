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

import cn.hutool.core.collection.CollectionUtil
import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseDTO
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Getter
@Setter
class DeployDto : BaseDTO(), Serializable {
    /**
     * 部署编号
     */
    private val id: String? = null
    private val app: AppDto? = null

    /**
     * 服务器
     */
    private val deploys: Set<ServerDeployDto?>? = null
    val servers: String? = null
        get() = if (CollectionUtil.isNotEmpty(deploys)) {
            deploys!!.stream().map { obj: ServerDeployDto? -> obj.getName() }.collect(Collectors.joining(","))
        } else field

    /**
     * 服务状态
     */
    private val status: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val deployDto = o as DeployDto
        return id == deployDto.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}