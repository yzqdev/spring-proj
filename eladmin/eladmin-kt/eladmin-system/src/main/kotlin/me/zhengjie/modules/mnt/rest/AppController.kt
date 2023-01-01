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
package me.zhengjie.modules.mnt.rest

import io.swagger.v3.oas.annotations.Operationimport

io.swagger.v3.oas.annotations.tags.Tagimport me.zhengjie.annotation .Logimport me.zhengjie.modules.mnt.domain.Appimport me.zhengjie.modules.mnt.service.AppServiceimport me.zhengjie.modules.mnt.service.dto.AppQueryCriteriaimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import java.io.IOExceptionimport

javax.servlet.http.HttpServletResponse
/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@Tag(name = "运维：应用管理")
@RequestMapping("/api/app")
class AppController(private val appService: AppService) {
    @Operation(summary = "导出应用数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('app:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: AppQueryCriteria?) {
        appService.download(appService.queryAll(criteria), response)
    }

    @Operation(summary = "查询应用")
    @GetMapping
    @PreAuthorize("@el.check('app:list')")
    fun query(criteria: AppQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(appService.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("新增应用")
    @Operation(summary = "新增应用")
    @PostMapping
    @PreAuthorize("@el.check('app:add')")
    fun create(@Validated @RequestBody resources: App): ResponseEntity<Any> {
        appService.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改应用")
    @Operation(summary = "修改应用")
    @PutMapping
    @PreAuthorize("@el.check('app:edit')")
    fun update(@Validated @RequestBody resources: App): ResponseEntity<Any> {
        appService.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除应用")
    @Operation(summary = "删除应用")
    @DeleteMapping
    @PreAuthorize("@el.check('app:del')")
    fun delete(@RequestBody ids: Set<Long>): ResponseEntity<Any> {
        appService.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }
}