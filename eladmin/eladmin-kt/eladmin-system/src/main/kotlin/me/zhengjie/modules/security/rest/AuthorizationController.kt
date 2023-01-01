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
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "系统：系统授权接口")
class AuthorizationController {
    private val properties: SecurityProperties? = null
    private val redisUtils: RedisUtils? = null
    private val onlineUserService: OnlineUserService? = null
    private val tokenProvider: TokenProvider? = null
    private val authenticationManagerBuilder: AuthenticationManagerBuilder? = null

    @Resource
    private val loginProperties: LoginProperties? = null

    @Operation(summary = "登录授权")
    @AnonymousPostMapping(  "/login")
    @Throws(
        Exception::class
    )
    fun login(@Validated @RequestBody authUser: AuthUserDto, request: HttpServletRequest?): ResponseEntity<Any> {
        // 密码解密
        val password = decryptByPrivateKey(RsaProperties.privateKey, authUser.password)
        // 查询验证码
        val code = redisUtils!![authUser.uuid] as String?
        // 清除验证码
        redisUtils.del(authUser.uuid)
        if (StringUtils.isBlank(code)) {
            throw BadRequestException("验证码不存在或已过期")
        }
        if (StringUtils.isBlank(authUser.code) || loginProperties.getCaptcha().verify(authUser.code)) {
            throw BadRequestException("验证码错误")
        }
        val authenticationToken = UsernamePasswordAuthenticationToken(authUser.username, password)
        val authentication = authenticationManagerBuilder!!.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        // 生成令牌与第三方系统获取令牌方式
        // UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        val token = tokenProvider!!.createToken(authentication)
        val jwtUserDto = authentication.principal as JwtUserDto
        // 保存在线信息
        onlineUserService!!.save(jwtUserDto, token, request)
        // 返回 token 与 用户信息
        val authInfo: Map<String, Any> = object : HashMap<String?, Any?>(2) {
            init {
                put("token", properties.getTokenStartWith() + token)
                put("user", jwtUserDto)
            }
        }
        if (loginProperties!!.isSingleLogin) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.username, token)
        }
        return ResponseEntity.ok(authInfo)
    }

    @get:GetMapping(value = ["/info"])
    @get:Operation(summary = "获取用户信息")
    val userInfo: ResponseEntity<Any>
        get() = ResponseEntity.ok(SecurityUtils.getCurrentUser())

    // 获取运算的结果
    @get:AnonymousGetMapping(value = "/code")
    @get:Operation(summary = "获取验证码")
    val code: ResponseEntity<Any>
        get() {
            // 获取运算的结果
            val captcha = loginProperties.getCaptcha()
            val uuid = properties.getCodeKey() + IdUtil.simpleUUID()
            //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
            val captchaValue = captcha!!.code
            //if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            //    captchaValue = captchaValue.split("\\.")[0];
            //}
            // 保存
            redisUtils!![uuid, captchaValue, loginProperties.getLoginCode().expiration] = TimeUnit.MINUTES
            // 验证码信息
            val imgResult: Map<String, Any> = object : HashMap<String?, Any?>(2) {
                init {
                    put("img", captcha!!.imageBase64Data)
                    put("uuid", uuid)
                }
            }
            return ResponseEntity.ok(imgResult)
        }

    @Operation(summary = "退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Any> {
        onlineUserService!!.logout(tokenProvider!!.getToken(request))
        return ResponseEntity(HttpStatus.OK)
    }
}