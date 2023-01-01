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
import lombok.extern.slf4j.Slf4j
import me.zhengjie.annotation.Log
import me.zhengjie.service.dto.QiniuQueryCriteria
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody
import java.io.IOException

/**
 * 发送邮件
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qiNiuContent")
@Tag(name = "工具：七牛云存储管理")
class QiniuController {
    private val qiNiuService: QiNiuService? = null
    @GetMapping(value = ["/config"])
    fun queryConfig(): ResponseEntity<Any> {
        return ResponseEntity<Any?>(qiNiuService.find(), HttpStatus.OK)
    }

    @Log("配置七牛云存储")
    @Operation(summary = "配置七牛云存储")
    @PutMapping(value = ["/config"])
    fun updateConfig(@Validated @RequestBody qiniuConfig: QiniuConfig): ResponseEntity<Any> {
        qiNiuService.config(qiniuConfig)
        qiNiuService.update(qiniuConfig.getType())
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @Operation(summary = "导出数据")
    @GetMapping(value = ["/download"])
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: QiniuQueryCriteria?) {
        qiNiuService.downloadList(qiNiuService.queryAll(criteria), response)
    }

    @Operation(summary = "查询文件")
    @GetMapping
    fun query(criteria: QiniuQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity<Any?>(qiNiuService.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("上传文件")
    @Operation(summary = "上传文件")
    @PostMapping
    fun upload(@RequestParam file: MultipartFile?): ResponseEntity<Any> {
        val qiniuContent: QiniuContent = qiNiuService.upload(file, qiNiuService.find())
        val map: MutableMap<String, Any> = HashMap(3)
        map["id"] = qiniuContent.getId()
        map["errno"] = 0
        map["data"] = arrayOf<String>(qiniuContent.getUrl())
        return ResponseEntity<Any>(map, HttpStatus.OK)
    }

    @Log("同步七牛云数据")
    @Operation(summary = "同步七牛云数据")
    @PostMapping(value = ["/synchronize"])
    fun synchronize(): ResponseEntity<Any> {
        qiNiuService.synchronize(qiNiuService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @Log("下载文件")
    @Operation(summary = "下载文件")
    @GetMapping(value = ["/download/{id}"])
    fun download(@PathVariable id: Long?): ResponseEntity<Any> {
        val map: MutableMap<String, Any> = HashMap(1)
        map["url"] = qiNiuService.download(qiNiuService.findByContentId(id), qiNiuService.find())
        return ResponseEntity<Any>(map, HttpStatus.OK)
    }

    @Log("删除文件")
    @Operation(summary = "删除文件")
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: Long?): ResponseEntity<Any> {
        qiNiuService.delete(qiNiuService.findByContentId(id), qiNiuService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @Log("删除多张图片")
    @Operation(summary = "删除多张图片")
    @DeleteMapping
    fun deleteAll(@RequestBody ids: Array<Long?>?): ResponseEntity<Any> {
        qiNiuService.deleteAll(ids, qiNiuService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}