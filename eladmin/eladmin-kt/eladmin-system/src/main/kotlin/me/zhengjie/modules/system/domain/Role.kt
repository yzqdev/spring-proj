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

io.swagger.v3.oas.annotations.media.Schemaimport lombok.*import me.zhengjie.base.BaseEntityimport

me.zhengjie.utils.enums.DataScopeEnumimport java.io.Serializableimport java.util.*import javax.persistence.*
import javax.validation.constraints.NotBlankimport

javax.validation.constraints.NotNull
/**
 * 角色
 * @author Zheng Jie
 * @date 2018-11-22
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role")
class Role : BaseEntity(), Serializable {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "ID", hidden = true)
    private val id: @NotNull(groups = [Update::class]) Long? = null

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "roles")
    @Schema(name = "用户", hidden = true)
    private val users: Set<User>? = null

    @ManyToMany
    @JoinTable(
        name = "sys_roles_menus",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "menu_id", referencedColumnName = "menu_id")]
    )
    @Schema(name = "菜单", hidden = true)
    private val menus: Set<Menu>? = null

    @ManyToMany
    @JoinTable(
        name = "sys_roles_depts",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "dept_id", referencedColumnName = "dept_id")]
    )
    @Schema(name = "部门", hidden = true)
    private val depts: Set<Dept>? = null

    @Schema(name = "名称", hidden = true)
    private val name: @NotBlank String? = null

    @Schema(name = "数据权限，全部 、 本级 、 自定义")
    private val dataScope: String = DataScopeEnum.THIS_LEVEL.getValue()

    @Column(name = "level")
    @Schema(name = "级别，数值越小，级别越大")
    private val level = 3

    @Schema(name = "描述")
    private val description: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val role = o as Role
        return id == role.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}