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

io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.modules.mnt.service.DeployHistoryServiceimport me.zhengjie.modules.mnt.service.dto.DeployHistoryQueryCriteriaimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.web.bind.annotation .*import java.io.IOExceptionimport

javax.servlet.http.HttpServletResponse
/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "运维：部署历史管理")
@RequestMapping("/api/deployHistory")
class DeployHistoryController {
    private val deployhistoryService: DeployHistoryService? = null

    @Operation(summary = "导出部署历史数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('deployHistory:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: DeployHistoryQueryCriteria?) {
        deployhistoryService!!.download(deployhistoryService.queryAll(criteria), response)
    }

    @Operation(summary = "查询部署历史")
    @GetMapping
    @PreAuthorize("@el.check('deployHistory:list')")
    fun query(criteria: DeployHistoryQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(deployhistoryService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("删除DeployHistory")
    @Operation(summary = "删除部署历史")
    @DeleteMapping
    @PreAuthorize("@el.check('deployHistory:del')")
    fun delete(@RequestBody ids: Set<String>): ResponseEntity<Any> {
        deployhistoryService!!.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }
}