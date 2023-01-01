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

io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.mnt.domain.Databaseimport me.zhengjie.modules.mnt.service.DatabaseServiceimport me.zhengjie.modules.mnt.service.dto.DatabaseQueryCriteriaimport me.zhengjie.modules.mnt.util.SqlUtilsimport me.zhengjie.utils.FileUtilimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import org.springframework.web.multipart.MultipartFileimport

java.io.Fileimport java.io.IOExceptionimport javax.servlet.http.HttpServletRequestimport javax.servlet.http.HttpServletResponse
/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Tag(name = "运维：数据库管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/database")
class DatabaseController {
    private val fileSavePath: String = FileUtil.getTmpDirPath() + "/"
    private val databaseService: DatabaseService? = null

    @Operation(summary = "导出数据库数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('database:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: DatabaseQueryCriteria?) {
        databaseService!!.download(databaseService.queryAll(criteria), response)
    }

    @Operation(summary = "查询数据库")
    @GetMapping
    @PreAuthorize("@el.check('database:list')")
    fun query(criteria: DatabaseQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(databaseService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("新增数据库")
    @Operation(summary = "新增数据库")
    @PostMapping
    @PreAuthorize("@el.check('database:add')")
    fun create(@Validated @RequestBody resources: Database): ResponseEntity<Any> {
        databaseService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改数据库")
    @Operation(summary = "修改数据库")
    @PutMapping
    @PreAuthorize("@el.check('database:edit')")
    fun update(@Validated @RequestBody resources: Database): ResponseEntity<Any> {
        databaseService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除数据库")
    @Operation(summary = "删除数据库")
    @DeleteMapping
    @PreAuthorize("@el.check('database:del')")
    fun delete(@RequestBody ids: Set<String>): ResponseEntity<Any> {
        databaseService!!.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    @Log("测试数据库链接")
    @Operation(summary = "测试数据库链接")
    @PostMapping("/testConnect")
    @PreAuthorize("@el.check('database:testConnect')")
    fun testConnect(@Validated @RequestBody resources: Database): ResponseEntity<Any> {
        return ResponseEntity(databaseService!!.testConnection(resources), HttpStatus.CREATED)
    }

    @Log("执行SQL脚本")
    @Operation(summary = "执行SQL脚本")
    @PostMapping(value = ["/upload"])
    @PreAuthorize("@el.check('database:add')")
    @Throws(
        Exception::class
    )
    fun upload(@RequestBody file: MultipartFile, request: HttpServletRequest): ResponseEntity<Any> {
        val id = request.getParameter("id")
        val database = databaseService!!.findById(id)
        val fileName: String?
        return if (database != null) {
            fileName = file.originalFilename
            val executeFile = File(fileSavePath + fileName)
            FileUtil.del(executeFile)
            file.transferTo(executeFile)
            val result = SqlUtils.executeFile(
                database.jdbcUrl,
                database.userName,
                database.pwd,
                executeFile
            )
            ResponseEntity(result, HttpStatus.OK)
        } else {
            throw BadRequestException("Database not exist")
        }
    }
}