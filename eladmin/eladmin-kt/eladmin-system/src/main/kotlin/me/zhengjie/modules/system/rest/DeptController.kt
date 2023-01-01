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

io.swagger.v3.oas.annotations.Operationimport io.swagger.v3.oas.annotations.tags.Tagimport lombok.extern.slf4j.Slf4jimport me.zhengjie.annotation .Logimport me.zhengjie.base.BaseEntityimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.system.domain.Deptimport me.zhengjie.modules.system.service.DeptServiceimport me.zhengjie.modules.system.service.dto.DeptDtoimport me.zhengjie.modules.system.service.dto.DeptQueryCriteriaimport me.zhengjie.utils.PageUtil.toPageimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-03-25
 */
@RestController
@Tag(name = "系统：部门管理")
@RequestMapping("/api/dept")
@Slf4j
class DeptController(private val deptService: DeptService) {
    @Operation(summary = "导出部门数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('dept:list')")
    @Throws(
        Exception::class
    )
    fun download(response: HttpServletResponse?, criteria: DeptQueryCriteria?) {
        deptService.download(deptService.queryAll(criteria, false), response)
    }

    @Operation(summary = "查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    @Throws(
        Exception::class
    )
    fun query(criteria: DeptQueryCriteria?): ResponseEntity<Any> {
        val deptDtos = deptService.queryAll(criteria, true)
        return ResponseEntity(toPage(deptDtos, deptDtos.size), HttpStatus.OK)
    }

    @Operation(summary = "查询部门:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('user:list','dept:list')")
    fun getSuperior(@RequestBody ids: List<Long?>): ResponseEntity<Any> {
        val deptDtos: MutableSet<DeptDto> = LinkedHashSet()
        for (id in ids) {
            val deptDto = deptService.findById(id)
            val depts = deptService.getSuperior(deptDto, ArrayList())
            deptDtos.addAll(depts)
        }
        return ResponseEntity(deptService.buildTree(ArrayList(deptDtos)), HttpStatus.OK)
    }

    @Log("新增部门")
    @Operation(summary = "新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    fun create(@Validated @RequestBody resources: Dept): ResponseEntity<Any> {
        if (resources.id != null) {
            throw BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID")
        }
        deptService.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改部门")
    @Operation(summary = "修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    fun update(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: Dept?
    ): ResponseEntity<Any> {
        deptService.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除部门")
    @Operation(summary = "删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    fun delete(@RequestBody ids: Set<Long?>): ResponseEntity<Any> {
        var deptDtos: MutableSet<DeptDto?> = HashSet()
        for (id in ids) {
            val deptList = deptService.findByPid(id!!)
            deptDtos.add(deptService.findById(id))
            if (CollectionUtil.isNotEmpty(deptList)) {
                deptDtos = deptService.getDeleteDepts(deptList, deptDtos)
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos)
        deptService.delete(deptDtos)
        return ResponseEntity(HttpStatus.OK)
    }

    companion object {
        private const val ENTITY_NAME = "dept"
    }
}