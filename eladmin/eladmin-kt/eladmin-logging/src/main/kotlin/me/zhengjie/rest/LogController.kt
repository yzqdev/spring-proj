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
package me.zhengjie.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import me.zhengjie.annotation.Log
import me.zhengjie.service.LogService
import me.zhengjie.service.dto.LogQueryCriteria
import me.zhengjie.utils.SecurityUtils
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Tag(name = "系统：日志管理")
class LogController {
    private val logService: LogService? = null

    @Log("导出数据")
    @Operation(summary = "导出数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check()")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: LogQueryCriteria) {
        criteria.logType = "INFO"
        logService!!.download(logService.queryAll(criteria), response)
    }

    @Log("导出错误数据")
    @Operation(summary = "导出错误数据")
    @GetMapping(value = ["/error/download"])
    @PreAuthorize("@el.check()")
    @Throws(
        IOException::class
    )
    fun downloadErrorLog(response: HttpServletResponse?, criteria: LogQueryCriteria) {
        criteria.logType = "ERROR"
        logService!!.download(logService.queryAll(criteria), response)
    }

    @GetMapping
    @Operation(summary = "日志查询")
    @PreAuthorize("@el.check()")
    fun query(criteria: LogQueryCriteria, pageable: Pageable?): ResponseEntity<Any> {
        criteria.logType = "INFO"
        return ResponseEntity(logService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @GetMapping(value = ["/user"])
    @Operation(summary = "用户日志查询")
    fun queryUserLog(criteria: LogQueryCriteria, pageable: Pageable?): ResponseEntity<Any> {
        println("LogController.queryUserLog")
        println(pageable)
        criteria.logType = "INFO"
        criteria.blurry = SecurityUtils.getCurrentUsername()
        println(criteria)
        return ResponseEntity(logService!!.queryAllByUser(criteria, pageable), HttpStatus.OK)
    }

    @GetMapping(value = ["/error"])
    @Operation(summary = "错误日志查询")
    @PreAuthorize("@el.check()")
    fun queryErrorLog(criteria: LogQueryCriteria, pageable: Pageable?): ResponseEntity<Any> {
        criteria.logType = "ERROR"
        return ResponseEntity(logService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @GetMapping(value = ["/error/{id}"])
    @Operation(summary = "日志异常详情查询")
    @PreAuthorize("@el.check()")
    fun queryErrorLogs(@PathVariable id: Long): ResponseEntity<Any> {
        return ResponseEntity(logService!!.findByErrDetail(id), HttpStatus.OK)
    }

    @DeleteMapping(value = ["/del/error"])
    @Log("删除所有ERROR日志")
    @Operation(summary = "删除所有ERROR日志")
    @PreAuthorize("@el.check()")
    fun delAllErrorLog(): ResponseEntity<Any> {
        logService!!.delAllByError()
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping(value = ["/del/info"])
    @Log("删除所有INFO日志")
    @Operation(summary = "删除所有INFO日志")
    @PreAuthorize("@el.check()")
    fun delAllInfoLog(): ResponseEntity<Any> {
        logService!!.delAllByInfo()
        return ResponseEntity(HttpStatus.OK)
    }
}