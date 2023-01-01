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
package me.zhengjie.modules.mnt.domain

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "mnt_server")
class ServerDeploy : BaseEntity(), Serializable {
    @Id
    @Column(name = "server_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Schema(name = "服务器名称")
    private val name: String? = null

    @Schema(name = "IP")
    private val ip: String? = null

    @Schema(name = "端口")
    private val port: Int? = null

    @Schema(name = "账号")
    private val account: String? = null

    @Schema(name = "密码")
    private val password: String? = null
    fun copy(source: ServerDeploy?) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true))
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as ServerDeploy
        return id == that.id && name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }
}