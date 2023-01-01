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
import me.zhengjie.domain.ColumnInfo
import me.zhengjie.exception.BadRequestException
import me.zhengjie.service.GenConfigService
import me.zhengjie.service.GeneratorService
import me.zhengjie.utils.PageUtil
import me.zhengjie.utils.PageUtil.toPage
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-01-02
 */
@RestController
@RequestMapping("/api/generator")
@Tag(name = "系统：代码生成管理")
class GeneratorController(
    private val generatorService: GeneratorService,
    private val genConfigService: GenConfigService
) {
    @Value("\${generator.enabled}")
    private val generatorEnabled: Boolean? = null
    @Operation(summary = "查询数据库数据")
    @GetMapping(value = ["/tables/all"])
    fun queryTables(): ResponseEntity<Any> {
        return ResponseEntity(generatorService.tables, HttpStatus.OK)
    }

    @Operation(summary = "查询数据库数据")
    @GetMapping(value = ["/tables"])
    fun queryTables(
        @RequestParam(defaultValue = "") name: String?,
        @RequestParam(defaultValue = "0") page: Int?,
        @RequestParam(defaultValue = "10") size: Int?
    ): ResponseEntity<Any> {
        val startEnd: IntArray = PageUtil.transToStartEnd(page, size)
        return ResponseEntity(generatorService.getTables(name!!, startEnd), HttpStatus.OK)
    }

    @Operation(summary = "查询字段数据")
    @GetMapping(value = ["/columns"])
    fun queryColumns(@RequestParam tableName: String?): ResponseEntity<Any> {
        val columnInfos = generatorService.getColumns(tableName)
        return ResponseEntity(toPage(columnInfos!!, columnInfos.size), HttpStatus.OK)
    }

    @Operation(summary = "保存字段数据")
    @PutMapping
    fun save(@RequestBody columnInfos: List<ColumnInfo?>?): ResponseEntity<HttpStatus> {
        generatorService.save(columnInfos!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "同步字段数据")
    @PostMapping(value = ["sync"])
    fun sync(@RequestBody tables: List<String?>): ResponseEntity<HttpStatus> {
        for (table in tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table))
        }
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "生成代码")
    @PostMapping(value = ["/{tableName}/{type}"])
    fun generator(
        @PathVariable tableName: String?,
        @PathVariable type: Int,
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): ResponseEntity<Any> {
        if (!generatorEnabled!! && type == 0) {
            throw BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！")
        }
        when (type) {
            0 -> generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName))
            1 -> return generatorService.preview(
                genConfigService.find(tableName),
                generatorService.getColumns(tableName)
            )

            2 -> generatorService.download(
                genConfigService.find(tableName),
                generatorService.getColumns(tableName),
                request,
                response
            )

            else -> throw BadRequestException("没有这个选项")
        }
        return ResponseEntity(HttpStatus.OK)
    }
}