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

io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.modules.mnt.domain.ServerDeployimport me.zhengjie.modules.mnt.service.ServerDeployServiceimport me.zhengjie.modules.mnt.service.dto.ServerDeployQueryCriteriaimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import java.io.IOExceptionimport

javax.servlet.http.HttpServletResponse
/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@Tag(name = "运维：服务器管理")
@RequiredArgsConstructor
@RequestMapping("/api/serverDeploy")
class ServerDeployController {
    private val serverDeployService: ServerDeployService? = null

    @Operation(summary = "导出服务器数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('serverDeploy:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: ServerDeployQueryCriteria?) {
        serverDeployService!!.download(serverDeployService.queryAll(criteria), response)
    }

    @Operation(summary = "查询服务器")
    @GetMapping
    @PreAuthorize("@el.check('serverDeploy:list')")
    fun query(criteria: ServerDeployQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(serverDeployService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("新增服务器")
    @Operation(summary = "新增服务器")
    @PostMapping
    @PreAuthorize("@el.check('serverDeploy:add')")
    fun create(@Validated @RequestBody resources: ServerDeploy): ResponseEntity<Any> {
        serverDeployService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改服务器")
    @Operation(summary = "修改服务器")
    @PutMapping
    @PreAuthorize("@el.check('serverDeploy:edit')")
    fun update(@Validated @RequestBody resources: ServerDeploy): ResponseEntity<Any> {
        serverDeployService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除服务器")
    @Operation(summary = "删除Server")
    @DeleteMapping
    @PreAuthorize("@el.check('serverDeploy:del')")
    fun delete(@RequestBody ids: Set<Long>): ResponseEntity<Any> {
        serverDeployService!!.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    @Log("测试连接服务器")
    @Operation(summary = "测试连接服务器")
    @PostMapping("/testConnect")
    @PreAuthorize("@el.check('serverDeploy:add')")
    fun testConnect(@Validated @RequestBody resources: ServerDeploy): ResponseEntity<Any> {
        return ResponseEntity(serverDeployService!!.testConnect(resources), HttpStatus.CREATED)
    }
}