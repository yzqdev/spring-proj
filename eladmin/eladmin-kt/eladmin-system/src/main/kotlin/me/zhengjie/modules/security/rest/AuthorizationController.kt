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

import cn.hutool.core.util.IdUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import me.zhengjie.annotation.rest.AnonymousDeleteMapping
import me.zhengjie.annotation.rest.AnonymousGetMapping
import me.zhengjie.annotation.rest.AnonymousPostMapping
import me.zhengjie.config.RsaProperties
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.security.config.bean.LoginProperties
import me.zhengjie.modules.security.config.bean.SecurityProperties
import me.zhengjie.modules.security.security.TokenProvider
import me.zhengjie.modules.security.service.OnlineUserService
import me.zhengjie.modules.security.service.dto.AuthUserDto
import me.zhengjie.modules.security.service.dto.JwtUserDto
import me.zhengjie.utils.*
import me.zhengjie.utils.RsaUtils.decryptByPrivateKey
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * ???????????????token????????????????????????
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "???????????????????????????")
class AuthorizationController {
    private val properties: SecurityProperties? = null
    private val redisUtils: RedisUtils? = null
    private val onlineUserService: OnlineUserService? = null
    private val tokenProvider: TokenProvider? = null
    private val authenticationManagerBuilder: AuthenticationManagerBuilder? = null

    @Resource
    private val loginProperties: LoginProperties? = null

    @Operation(summary = "????????????")
    @AnonymousPostMapping(  "/login")
    @Throws(
        Exception::class
    )
    fun login(@Validated @RequestBody authUser: AuthUserDto, request: HttpServletRequest?): ResponseEntity<Any> {
        // ????????????
        val password = decryptByPrivateKey(RsaProperties.privateKey, authUser.password)
        // ???????????????
        val code = redisUtils!![authUser.uuid] as String?
        // ???????????????
        redisUtils.del(authUser.uuid)
        if (StringUtils.isBlank(code)) {
            throw BadRequestException("??????????????????????????????")
        }
        if (StringUtils.isBlank(authUser.code) || loginProperties.getCaptcha().verify(authUser.code)) {
            throw BadRequestException("???????????????")
        }
        val authenticationToken = UsernamePasswordAuthenticationToken(authUser.username, password)
        val authentication = authenticationManagerBuilder!!.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        // ????????????????????????????????????????????????
        // UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        val token = tokenProvider!!.createToken(authentication)
        val jwtUserDto = authentication.principal as JwtUserDto
        // ??????????????????
        onlineUserService!!.save(jwtUserDto, token, request)
        // ?????? token ??? ????????????
        val authInfo: Map<String, Any> = object : HashMap<String?, Any?>(2) {
            init {
                put("token", properties.getTokenStartWith() + token)
                put("user", jwtUserDto)
            }
        }
        if (loginProperties!!.isSingleLogin) {
            //???????????????????????????token
            onlineUserService.checkLoginOnUser(authUser.username, token)
        }
        return ResponseEntity.ok(authInfo)
    }

    @get:GetMapping(value = ["/info"])
    @get:Operation(summary = "??????????????????")
    val userInfo: ResponseEntity<Any>
        get() = ResponseEntity.ok(SecurityUtils.getCurrentUser())

    // ?????????????????????
    @get:AnonymousGetMapping(value = "/code")
    @get:Operation(summary = "???????????????")
    val code: ResponseEntity<Any>
        get() {
            // ?????????????????????
            val captcha = loginProperties.getCaptcha()
            val uuid = properties.getCodeKey() + IdUtil.simpleUUID()
            //????????????????????? arithmetic???????????? >= 2 ??????captcha.text()??????????????????????????????
            val captchaValue = captcha!!.code
            //if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            //    captchaValue = captchaValue.split("\\.")[0];
            //}
            // ??????
            redisUtils!![uuid, captchaValue, loginProperties.getLoginCode().expiration] = TimeUnit.MINUTES
            // ???????????????
            val imgResult: Map<String, Any> = object : HashMap<String?, Any?>(2) {
                init {
                    put("img", captcha!!.imageBase64Data)
                    put("uuid", uuid)
                }
            }
            return ResponseEntity.ok(imgResult)
        }

    @Operation(summary = "????????????")
    @AnonymousDeleteMapping(value = "/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Any> {
        onlineUserService!!.logout(tokenProvider!!.getToken(request))
        return ResponseEntity(HttpStatus.OK)
    }
}