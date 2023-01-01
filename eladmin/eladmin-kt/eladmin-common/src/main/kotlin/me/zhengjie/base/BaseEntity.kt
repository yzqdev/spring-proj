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
package me.zhengjie.base

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter
import lombok.Setter
import org.apache.commons.lang3.builder.ToStringBuilder
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

/**
 * 通用字段， is_del 根据需求自行添加
 * @author Zheng Jie
 * @Date 2019年10月24日20:46:32
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity : Serializable {
    @CreatedBy
    @Column(name = "create_by", updatable = false)
    @Schema(name = "创建人", hidden = true)
    private val createBy: String? = null

    @LastModifiedBy
    @Column(name = "update_by")
    @Schema(name = "更新人", hidden = true)
    private val updateBy: String? = null

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @Schema(name = "创建时间", hidden = true)
    private val createTime: Timestamp? = null

    @UpdateTimestamp
    @Column(name = "update_time")
    @Schema(name = "更新时间", hidden = true)
    private val updateTime: Timestamp? = null

    /* 分组校验 */
    annotation class Create

    /* 分组校验 */
    annotation class Update

    override fun toString(): String {
        val builder = ToStringBuilder(this)
        val fields = this.javaClass.declaredFields
        try {
            for (f in fields) {
                f.isAccessible = true
                builder.append(f.name, f[this]).append("\n")
            }
        } catch (e: Exception) {
            builder.append("toString builder encounter an error")
        }
        return builder.toString()
    }
}