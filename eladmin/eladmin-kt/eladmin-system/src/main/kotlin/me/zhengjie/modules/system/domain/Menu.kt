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
import javax.validation.constraints.NotNull

/**
 * @author Zheng Jie
 * @date 2018-12-17
 */
@Entity
@Getter
@Setter
@Table(name = "sys_menu")
class Menu : BaseEntity(), Serializable {
    @Id
    @Column(name = "menu_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: @NotNull(groups = [Update::class]) Long? = null

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "menus")
    @Schema(name = "菜单角色")
    private val roles: Set<Role>? = null

    @Schema(name = "菜单标题")
    private val title: String? = null

    @Column(name = "name")
    @Schema(name = "菜单组件名称")
    private val componentName: String? = null

    @Schema(name = "排序")
    private val menuSort = 999

    @Schema(name = "组件路径")
    private val component: String? = null

    @Schema(name = "路由地址")
    private val path: String? = null

    @Schema(name = "菜单类型，目录、菜单、按钮")
    private val type: Int? = null

    @Schema(name = "权限标识")
    private val permission: String? = null

    @Schema(name = "菜单图标")
    private val icon: String? = null

    @Column(columnDefinition = "bit(1) default 0")
    @Schema(name = "缓存")
    private val cache: Boolean? = null

    @Column(columnDefinition = "bit(1) default 0")
    @Schema(name = "是否隐藏")
    private val hidden: Boolean? = null

    @Schema(name = "上级菜单")
    private val pid: Long? = null

    @Schema(name = "子节点数目", hidden = true)
    private val subCount = 0

    @Schema(name = "外链菜单")
    private val iFrame: Boolean? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val menu = o as Menu
        return id == menu.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}