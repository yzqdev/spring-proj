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
package me.zhengjie.modules.system.rest

import cn.hutool.core.lang.Dictimport

io.swagger.v3.oas.annotations.Operationimport io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.base.BaseEntityimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.system.domain.Roleimport me.zhengjie.modules.system.service.RoleServiceimport me.zhengjie.modules.system.service.dto.RoleQueryCriteriaimport me.zhengjie.modules.system.service.dto.RoleSmallDtoimport me.zhengjie.utils.SecurityUtilsimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import java.io.IOExceptionimport

java.util.*import java.util.stream.Collectorsimport

javax.servlet.http.HttpServletResponse
/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：角色管理")
@RequestMapping("/api/roles")
class RoleController {
    private val roleService: RoleService? = null
    @Operation(summary = "获取单个role")
    @GetMapping(value = ["/{id}"])
    @PreAuthorize("@el.check('roles:list')")
    fun query(@PathVariable id: Long?): ResponseEntity<Any> {
        return ResponseEntity(roleService!!.findById(id!!), HttpStatus.OK)
    }

    @Operation(summary = "导出角色数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('role:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: RoleQueryCriteria?) {
        roleService!!.download(roleService.queryAll(criteria), response)
    }

    @Operation(summary = "返回全部的角色")
    @GetMapping(value = ["/all"])
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    fun query(): ResponseEntity<Any> {
        return ResponseEntity(roleService!!.queryAll(), HttpStatus.OK)
    }

    @Operation(summary = "查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    fun query(criteria: RoleQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(roleService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @get:GetMapping(value = ["/level"])
    @get:Operation(summary = "获取用户级别")
    val level: ResponseEntity<Any>
        get() = ResponseEntity(Dict.create().set("level", getLevels(null)), HttpStatus.OK)

    @Log("新增角色")
    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    fun create(@Validated @RequestBody resources: Role): ResponseEntity<Any> {
        if (resources.id != null) {
            throw BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID")
        }
        getLevels(resources.level)
        roleService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改角色")
    @Operation(summary = "修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    fun update(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: Role
    ): ResponseEntity<Any> {
        getLevels(resources.level)
        roleService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("修改角色菜单")
    @Operation(summary = "修改角色菜单")
    @PutMapping(value = ["/menu"])
    @PreAuthorize("@el.check('roles:edit')")
    fun updateMenu(@RequestBody resources: Role): ResponseEntity<Any> {
        val role = roleService!!.findById(resources.id)
        getLevels(role.level)
        roleService.updateMenu(resources, role)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除角色")
    @Operation(summary = "删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    fun delete(@RequestBody ids: Set<Long?>): ResponseEntity<Any> {
        for (id in ids) {
            val role = roleService!!.findById(id!!)
            getLevels(role.level)
        }
        // 验证是否被用户关联
        roleService!!.verification(ids)
        roleService.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private fun getLevels(level: Int?): Int {
        val levels = roleService!!.findByUsersId(SecurityUtils.getCurrentUserId()).stream()
            .map { obj: RoleSmallDto -> obj.level }
            .collect(Collectors.toList())
        val min = Collections.min(levels)
        if (level != null) {
            if (level < min) {
                throw BadRequestException("权限不足，你的角色级别：$min，低于操作的角色级别：$level")
            }
        }
        return min
    }

    companion object {
        private const val ENTITY_NAME = "role"
    }
}