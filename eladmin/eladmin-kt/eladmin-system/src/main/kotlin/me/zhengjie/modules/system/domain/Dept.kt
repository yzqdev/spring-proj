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
package me.zhengjie.modules.system.domain

import com.alibaba.fastjson.annotation.JSONFieldimport

io.swagger.v3.oas.annotations.media.Schemaimport lombok.Getterimport lombok.Setterimport me.zhengjie.base.BaseEntityimport java.io.Serializableimport java.util.*import javax.persistence.*
import javax.validation.constraints.NotBlankimport

javax.validation.constraints.NotNull
/**
 * @author Zheng Jie
 * @date 2019-03-25
 */
@Entity
@Getter
@Setter
@Table(name = "sys_dept")
class Dept : BaseEntity(), Serializable {
    @Id
    @Column(name = "dept_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: @NotNull(groups = [Update::class]) Long? = null

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "depts")
    @Schema(name = "角色")
    private val roles: Set<Role>? = null

    @Schema(name = "排序")
    private val deptSort: Int? = null

    @Schema(name = "部门名称")
    private val name: @NotBlank String? = null

    @Schema(name = "是否启用")
    private val enabled: @NotNull Boolean? = null

    @Schema(name = "上级部门")
    private val pid: Long? = null

    @Schema(name = "子节点数目", hidden = true)
    private val subCount = 0
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val dept = o as Dept
        return id == dept.id && name == dept.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }
}