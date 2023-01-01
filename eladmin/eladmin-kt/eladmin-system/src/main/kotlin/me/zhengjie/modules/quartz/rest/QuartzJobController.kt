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
package me.zhengjie.modules.quartz.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import me.zhengjie.annotation.Log
import me.zhengjie.base.BaseEntity
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.quartz.domain.QuartzJob
import me.zhengjie.modules.quartz.service.QuartzJobService
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
@Tag(name = "系统:定时任务管理")
class QuartzJobController {
    private val quartzJobService: QuartzJobService? = null
    @Operation(summary = "查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('timing:list')")
    fun query(criteria: JobQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(quartzJobService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Operation(summary = "导出任务数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('timing:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: JobQueryCriteria?) {
        quartzJobService!!.download(quartzJobService.queryAll(criteria), response)
    }

    @Operation(summary = "导出日志数据")
    @GetMapping(value = ["/logs/download"])
    @PreAuthorize("@el.check('timing:list')")
    @Throws(
        IOException::class
    )
    fun downloadLog(response: HttpServletResponse?, criteria: JobQueryCriteria?) {
        quartzJobService!!.downloadLog(quartzJobService.queryAllLog(criteria), response)
    }

    @Operation(summary = "查询任务执行日志")
    @GetMapping(value = ["/logs"])
    @PreAuthorize("@el.check('timing:list')")
    fun queryJobLog(criteria: JobQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(quartzJobService!!.queryAllLog(criteria, pageable), HttpStatus.OK)
    }

    @Log("新增定时任务")
    @Operation(summary = "新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('timing:add')")
    fun create(@Validated @RequestBody resources: QuartzJob): ResponseEntity<Any> {
        if (resources.id != null) {
            throw BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID")
        }
        quartzJobService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改定时任务")
    @Operation(summary = "修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('timing:edit')")
    fun update(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: QuartzJob
    ): ResponseEntity<Any> {
        quartzJobService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("更改定时任务状态")
    @Operation(summary = "更改定时任务状态")
    @PutMapping(value = ["/{id}"])
    @PreAuthorize("@el.check('timing:edit')")
    fun update(@PathVariable id: Long): ResponseEntity<Any> {
        quartzJobService!!.updateIsPause(quartzJobService.findById(id))
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("执行定时任务")
    @Operation(summary = "执行定时任务")
    @PutMapping(value = ["/exec/{id}"])
    @PreAuthorize("@el.check('timing:edit')")
    fun execution(@PathVariable id: Long): ResponseEntity<Any> {
        quartzJobService!!.execution(quartzJobService.findById(id))
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除定时任务")
    @Operation(summary = "删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('timing:del')")
    fun delete(@RequestBody ids: Set<Long>): ResponseEntity<Any> {
        quartzJobService!!.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    companion object {
        private const val ENTITY_NAME = "quartzJob"
    }
}