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
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "mnt_database")
class Database : BaseEntity(), Serializable {
    @Id
    @Column(name = "db_id")
    @Schema(name = "ID", hidden = true)
    private val id: String? = null

    @Schema(name = "数据库名称")
    private val name: String? = null

    @Schema(name = "数据库连接地址")
    private val jdbcUrl: String? = null

    @Schema(name = "数据库密码")
    private val pwd: String? = null

    @Schema(name = "用户名")
    private val userName: String? = null
    fun copy(source: Database?) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true))
    }
}