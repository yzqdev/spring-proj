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

import cn.hutool.core.collection.CollectionUtilimport

io.swagger.v3.oas.annotations.Operationimport io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.base.BaseEntityimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.system.domain.Menuimport me.zhengjie.modules.system.service.MenuServiceimport me.zhengjie.modules.system.service.dto.MenuDtoimport me.zhengjie.modules.system.service.dto.MenuQueryCriteriaimport me.zhengjie.modules.system.service.mapstruct.MenuMapperimport me.zhengjie.utils.PageUtil.toPageimport me.zhengjie.utils.SecurityUtilsimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import java.util.stream.Collectorsimport

javax.servlet.http.HttpServletResponse
/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：菜单管理")
@RequestMapping("/api/menus")
class MenuController {
    private val menuService: MenuService? = null
    private val menuMapper: MenuMapper? = null

    @Operation(summary = "导出菜单数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('menu:list')")
    @Throws(
        Exception::class
    )
    fun download(response: HttpServletResponse?, criteria: MenuQueryCriteria?) {
        menuService!!.download(menuService.queryAll(criteria, false), response)
    }

    @GetMapping(value = ["/all"])
    @Operation(summary = "获取前端所需菜单")
    fun allMenus(): ResponseEntity<Any> {
        val menuDtoList = menuService!!.findByUser(SecurityUtils.getCurrentUserId())
        val menuDtos = menuService.buildTree(menuDtoList)
        return ResponseEntity(menuService.buildMenus(menuDtos), HttpStatus.OK)
    }

    @Operation(summary = "返回全部的菜单")
    @GetMapping(value = ["/lazy"])
    @PreAuthorize("@el.check('menu:list','roles:list')")
    fun query(@RequestParam pid: Long?): ResponseEntity<Any> {
        return ResponseEntity(menuService!!.getMenus(pid), HttpStatus.OK)
    }

    @Operation(summary = "根据菜单ID返回所有子节点ID，包含自身ID")
    @GetMapping(value = ["/child"])
    @PreAuthorize("@el.check('menu:list','roles:list')")
    fun child(@RequestParam id: Long?): ResponseEntity<Any> {
        var menuSet: MutableSet<Menu> = HashSet()
        val menuList = menuService!!.getMenus(id)
        menuSet.add(menuService.findOne(id))
        menuSet = menuService.getChildMenus(menuMapper!!.toEntity(menuList), menuSet)
        val ids = menuSet.stream().map { obj: Menu -> obj.id }.collect(Collectors.toSet())
        return ResponseEntity(ids, HttpStatus.OK)
    }

    @GetMapping
    @Operation(summary = "查询菜单")
    @PreAuthorize("@el.check('menu:list')")
    @Throws(
        Exception::class
    )
    fun query(criteria: MenuQueryCriteria?): ResponseEntity<Any> {
        val menuDtoList = menuService!!.queryAll(criteria, true)
        return ResponseEntity(toPage(menuDtoList, menuDtoList.size), HttpStatus.OK)
    }

    @Operation(summary = "查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    fun getSuperior(@RequestBody ids: List<Long?>): ResponseEntity<Any> {
        val menuDtos: MutableSet<MenuDto> = LinkedHashSet()
        if (CollectionUtil.isNotEmpty(ids)) {
            for (id in ids) {
                val menuDto = menuService!!.findById(id!!)
                menuDtos.addAll(menuService.getSuperior(menuDto, ArrayList()))
            }
            return ResponseEntity(menuService!!.buildTree(ArrayList(menuDtos)), HttpStatus.OK)
        }
        return ResponseEntity(menuService!!.getMenus(null), HttpStatus.OK)
    }

    @Log("新增菜单")
    @Operation(summary = "新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    fun create(@Validated @RequestBody resources: Menu): ResponseEntity<Any> {
        if (resources.id != null) {
            throw BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID")
        }
        menuService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改菜单")
    @Operation(summary = "修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    fun update(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: Menu?
    ): ResponseEntity<Any> {
        menuService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除菜单")
    @Operation(summary = "删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    fun delete(@RequestBody ids: Set<Long?>): ResponseEntity<Any> {
        var menuSet: MutableSet<Menu?> = HashSet()
        for (id in ids) {
            val menuList = menuService!!.getMenus(id)
            menuSet.add(menuService.findOne(id))
            menuSet = menuService.getChildMenus(menuMapper!!.toEntity(menuList), menuSet)
        }
        menuService!!.delete(menuSet)
        return ResponseEntity(HttpStatus.OK)
    }

    companion object {
        private const val ENTITY_NAME = "menu"
    }
}