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
import me.zhengjie.service.EmailService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody

/**
 * 发送邮件
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/email")
@Tag(name = "工具：邮件管理")
class EmailController {
    private val emailService: EmailService? = null
    @GetMapping
    fun queryConfig(): ResponseEntity<Any> {
        return ResponseEntity<Any?>(emailService.find(), HttpStatus.OK)
    }

    @Log("配置邮件")
    @PutMapping
    @Operation(summary = "配置邮件")
    @Throws(
        Exception::class
    )
    fun updateConfig(@Validated @RequestBody emailConfig: EmailConfig?): ResponseEntity<Any> {
        emailService.config(emailConfig, emailService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @Log("发送邮件")
    @PostMapping
    @Operation(summary = "发送邮件")
    fun sendEmail(@Validated @RequestBody emailVo: EmailVo?): ResponseEntity<Any> {
        emailService.send(emailVo, emailService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}