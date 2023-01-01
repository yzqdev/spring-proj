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

import io.swagger.v3.oas.annotations.Operationimport

io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.base.BaseEntityimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.system.domain.DictDetailimport me.zhengjie.modules.system.service.DictDetailServiceimport me.zhengjie.modules.system.service.dto.DictDetailDtoimport me.zhengjie.modules.system.service.dto.DictDetailQueryCriteriaimport org.springframework.data .domain.Pageableimport org.springframework.data .domain.Sortimport org.springframework.data .web.PageableDefaultimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*
/**
 * @author Zheng Jie
 * @date 2019-04-10
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
class DictDetailController {
    private val dictDetailService: DictDetailService? = null
    @Operation(summary = "查询字典详情")
    @GetMapping
    fun query(
        criteria: DictDetailQueryCriteria?,
        @PageableDefault(sort = ["dictSort"], direction = Sort.Direction.ASC) pageable: Pageable?
    ): ResponseEntity<Any> {
        return ResponseEntity(dictDetailService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Operation(summary = "查询多个字典详情")
    @GetMapping(value = ["/map"])
    fun getDictDetailMaps(@RequestParam dictName: String): ResponseEntity<Any> {
        val names = dictName.split("[,，]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val dictMap: MutableMap<String, List<DictDetailDto>> = HashMap(16)
        for (name in names) {
            dictMap[name] = dictDetailService!!.getDictByName(name)
        }
        return ResponseEntity(dictMap, HttpStatus.OK)
    }

    @Log("新增字典详情")
    @Operation(summary = "新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    fun create(@Validated @RequestBody resources: DictDetail): ResponseEntity<Any> {
        if (resources.id != null) {
            throw BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID")
        }
        dictDetailService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改字典详情")
    @Operation(summary = "修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    fun update(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: DictDetail?
    ): ResponseEntity<Any> {
        dictDetailService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除字典详情")
    @Operation(summary = "删除字典详情")
    @DeleteMapping(value = ["/{id}"])
    @PreAuthorize("@el.check('dict:del')")
    fun delete(@PathVariable id: Long?): ResponseEntity<Any> {
        dictDetailService!!.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }

    companion object {
        private const val ENTITY_NAME = "dictDetail"
    }
}