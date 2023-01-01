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
import me.zhengjie.annotation.Log
import me.zhengjie.service.LocalStorageService
import me.zhengjie.utils.FileUtil
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody
import java.io.IOException

/**
 * @author Zheng Jie
 * @date 2019-09-05
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
class LocalStorageController {
    private val localStorageService: LocalStorageService? = null
    @Operation(summary = "查询文件")
    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    fun query(criteria: LocalStorageQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity<Any?>(localStorageService.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Operation(summary = "导出数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('storage:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: LocalStorageQueryCriteria?) {
        localStorageService.download(localStorageService.queryAll(criteria), response)
    }

    @Operation(summary = "上传文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    fun create(@RequestParam name: String?, @RequestParam("file") file: MultipartFile?): ResponseEntity<Any> {
        localStorageService.create(name, file)
        return ResponseEntity<Any>(HttpStatus.CREATED)
    }

    @PostMapping("/pictures")
    @Operation(summary = "上传图片")
    fun upload(@RequestParam file: MultipartFile): ResponseEntity<Any> {
        // 判断文件是否为图片
        val suffix = FileUtil.getExtensionName(file.getOriginalFilename())
        if (FileUtil.IMAGE != FileUtil.getFileType(suffix)) {
            throw BadRequestException("只能上传图片")
        }
        val localStorage: LocalStorage = localStorageService.create(null, file)
        return ResponseEntity<Any>(localStorage, HttpStatus.OK)
    }

    @Log("修改文件")
    @Operation(summary = "修改文件")
    @PutMapping
    @PreAuthorize("@el.check('storage:edit')")
    fun update(@Validated @RequestBody resources: LocalStorage?): ResponseEntity<Any> {
        localStorageService.update(resources)
        return ResponseEntity<Any>(HttpStatus.NO_CONTENT)
    }

    @Log("删除文件")
    @DeleteMapping
    @Operation(summary = "多选删除")
    fun delete(@RequestBody ids: Array<Long?>?): ResponseEntity<Any> {
        localStorageService.deleteAll(ids)
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}