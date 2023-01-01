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
package me.zhengjie.modules.system.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import me.zhengjie.service.EmailService
import org.springframework.http.HttpStatus
import java.util.*

/**
 * @author Zheng Jie
 * @date 2018-12-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
@Tag(name = "系统：验证码管理")
class VerifyController {
    private val verificationCodeService: VerifyService? = null
    private val emailService: EmailService? = null
    @PostMapping(value = ["/resetEmail"])
    @Operation(summary = "重置邮箱，发送验证码")
    fun resetEmail(@RequestParam email: String?): ResponseEntity<Any> {
        val emailVo: EmailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey())
        emailService.send(emailVo, emailService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping(value = ["/email/resetPass"])
    @Operation(summary = "重置密码，发送验证码")
    fun resetPass(@RequestParam email: String?): ResponseEntity<Any> {
        val emailVo: EmailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey())
        emailService.send(emailVo, emailService.find())
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @GetMapping(value = ["/validated"])
    @Operation(summary = "验证码验证")
    fun validated(
        @RequestParam email: String,
        @RequestParam code: String?,
        @RequestParam codeBi: Int?
    ): ResponseEntity<Any> {
        val biEnum: CodeBiEnum = CodeBiEnum.find(codeBi)
        when (Objects.requireNonNull<CodeBiEnum>(biEnum)) {
            CodeBiEnum.ONE -> verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code)
            CodeBiEnum.TWO -> verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code)
            else -> {}
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}