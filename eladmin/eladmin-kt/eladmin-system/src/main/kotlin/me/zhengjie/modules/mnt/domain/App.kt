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
import javax.persistence.*

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "mnt_app")
class App : BaseEntity(), Serializable {
    @Id
    @Column(name = "app_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Schema(name = "名称")
    private val name: String? = null

    @Schema(name = "端口")
    private val port = 0

    @Schema(name = "上传路径")
    private val uploadPath: String? = null

    @Schema(name = "部署路径")
    private val deployPath: String? = null

    @Schema(name = "备份路径")
    private val backupPath: String? = null

    @Schema(name = "启动脚本")
    private val startScript: String? = null

    @Schema(name = "部署脚本")
    private val deployScript: String? = null
    fun copy(source: App?) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true))
    }
}