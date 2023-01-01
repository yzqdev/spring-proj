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

io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.modules.mnt.domain.Deployimport me.zhengjie.modules.mnt.domain.DeployHistoryimport me.zhengjie.modules.mnt.service.DeployServiceimport me.zhengjie.modules.mnt.service.dto.DeployQueryCriteriaimport me.zhengjie.utils.FileUtilimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import org.springframework.web.multipart.MultipartFileimport

java.io.Fileimport java.io.IOExceptionimport java.util.*import javax.servlet.http.HttpServletRequestimport

javax.servlet.http.HttpServletResponse
/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@Tag(name = "运维：部署管理")
@RequiredArgsConstructor
@RequestMapping("/api/deploy")
class DeployController {
    private val fileSavePath: String = FileUtil.getTmpDirPath() + "/"
    private val deployService: DeployService? = null

    @Operation(summary = "导出部署数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('database:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: DeployQueryCriteria?) {
        deployService!!.download(deployService.queryAll(criteria), response)
    }

    @Operation(summary = "查询部署")
    @GetMapping
    @PreAuthorize("@el.check('deploy:list')")
    fun query(criteria: DeployQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(deployService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("新增部署")
    @Operation(summary = "新增部署")
    @PostMapping
    @PreAuthorize("@el.check('deploy:add')")
    fun create(@Validated @RequestBody resources: Deploy): ResponseEntity<Any> {
        deployService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("修改部署")
    @Operation(summary = "修改部署")
    @PutMapping
    @PreAuthorize("@el.check('deploy:edit')")
    fun update(@Validated @RequestBody resources: Deploy): ResponseEntity<Any> {
        deployService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("删除部署")
    @Operation(summary = "删除部署")
    @DeleteMapping
    @PreAuthorize("@el.check('deploy:del')")
    fun delete(@RequestBody ids: Set<Long>): ResponseEntity<Any> {
        deployService!!.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    @Log("上传文件部署")
    @Operation(summary = "上传文件部署")
    @PostMapping(value = ["/upload"])
    @PreAuthorize("@el.check('deploy:edit')")
    @Throws(
        Exception::class
    )
    fun upload(@RequestBody file: MultipartFile?, request: HttpServletRequest): ResponseEntity<Any> {
        val id = java.lang.Long.valueOf(request.getParameter("id"))
        var fileName: String? = ""
        if (file != null) {
            fileName = file.originalFilename
            val deployFile = File(fileSavePath + fileName)
            FileUtil.del(deployFile)
            file.transferTo(deployFile)
            //文件下一步要根据文件名字来
            deployService!!.deploy(fileSavePath + fileName, id)
        } else {
            println("没有找到相对应的文件")
        }
        println("文件上传的原名称为:" + Objects.requireNonNull(file).originalFilename)
        val map: MutableMap<String, Any?> = HashMap(2)
        map["errno"] = 0
        map["id"] = fileName
        return ResponseEntity(map, HttpStatus.OK)
    }

    @Log("系统还原")
    @Operation(summary = "系统还原")
    @PostMapping(value = ["/serverReduction"])
    @PreAuthorize("@el.check('deploy:edit')")
    fun serverReduction(@Validated @RequestBody resources: DeployHistory): ResponseEntity<Any> {
        val result = deployService!!.serverReduction(resources)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @Log("服务运行状态")
    @Operation(summary = "服务运行状态")
    @PostMapping(value = ["/serverStatus"])
    @PreAuthorize("@el.check('deploy:edit')")
    fun serverStatus(@Validated @RequestBody resources: Deploy): ResponseEntity<Any> {
        val result = deployService!!.serverStatus(resources)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @Log("启动服务")
    @Operation(summary = "启动服务")
    @PostMapping(value = ["/startServer"])
    @PreAuthorize("@el.check('deploy:edit')")
    fun startServer(@Validated @RequestBody resources: Deploy): ResponseEntity<Any> {
        val result = deployService!!.startServer(resources)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @Log("停止服务")
    @Operation(summary = "停止服务")
    @PostMapping(value = ["/stopServer"])
    @PreAuthorize("@el.check('deploy:edit')")
    fun stopServer(@Validated @RequestBody resources: Deploy): ResponseEntity<Any> {
        val result = deployService!!.stopServer(resources)
        return ResponseEntity(result, HttpStatus.OK)
    }
}