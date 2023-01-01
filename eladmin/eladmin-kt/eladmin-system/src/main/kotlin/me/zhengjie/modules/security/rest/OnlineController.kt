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
package me.zhengjie.modules.security.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import me.zhengjie.modules.security.service.OnlineUserService
import me.zhengjie.utils.EncryptUtils.desDecrypt
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Tag(name = "系统：在线用户管理")
@Slf4j
class OnlineController(private val onlineUserService: OnlineUserService) {
    val log = LoggerFactory.getLogger(this.javaClass)
    @Operation(summary = "查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check()")
    fun query(filter: String?, pageable: Pageable): ResponseEntity<Any> {
        return ResponseEntity(onlineUserService!!.getAll(filter, pageable), HttpStatus.OK)
    }

    @Operation(summary = "导出数据")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check()")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, filter: String?) {
     log.info(onlineUserService!!.getAll(filter).toString())
        onlineUserService.download(onlineUserService.getAll(filter), response)
    }

    @Operation(summary = "踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check()")
    @Throws(
        Exception::class
    )
    fun delete(@RequestBody keys:MutableSet<String>): ResponseEntity<Any> {
        for (key in keys) {
            // 解密Key

            onlineUserService.kickOut(desDecrypt(key))
        }
        return ResponseEntity(HttpStatus.OK)
    }
}