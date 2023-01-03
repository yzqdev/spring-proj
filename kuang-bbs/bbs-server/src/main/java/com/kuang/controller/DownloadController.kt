package com.kuang.controller

import com.kuang.model.entity.Download
import com.kuang.service.DownloadService
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

/**
 *
 *
 * 前端控制器
 *
 *
 * @author 遇见狂神说
 * @since 2020-07-08
 */
@RestController
@RequestMapping
@Schema(title = "下载")
class DownloadController {
    @Resource
    var downloadService: DownloadService? = null
    @GetMapping(["/download"])
    fun download(): List<Download?> {
        return downloadService!!.list()
    }
}