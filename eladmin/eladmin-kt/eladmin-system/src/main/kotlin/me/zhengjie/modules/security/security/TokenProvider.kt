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
package me.zhengjie.modules.security.security

import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.IdUtil
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.Claim
import lombok.extern.slf4j.Slf4j
import me.zhengjie.modules.security.config.bean.SecurityProperties
import me.zhengjie.utils.RedisUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest

/**
 * @author /
 */
@Slf4j
@Component
class TokenProvider(private val properties: SecurityProperties, private val redisUtils: RedisUtils) : InitializingBean {
    private var algorithm: Algorithm? = null
    override fun afterPropertiesSet() {
        val decoder = Base64.getMimeDecoder()
        val keyBytes = decoder.decode(properties.base64Secret)
        algorithm = Algorithm.HMAC256(keyBytes)
    }

    /**
     * 创建Token 设置永不过期，
     * Token 的时间有效性转到Redis 维护
     *
     * @param authentication /
     * @return /
     */
    fun createToken(authentication: Authentication): String {
        return JWT.create().withIssuer(IdUtil.simpleUUID()) // 加入ID确保生成的 Token 都不一致
            .withClaim(AUTHORITIES_KEY, authentication.name)
            .withSubject(authentication.name)
            .sign(algorithm)
    }

    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
    fun getAuthentication(token: String?): Authentication {
        val jwt = JWT.decode(token)
        val claims = jwt.claims
        val principal = User(jwt.subject, "******", ArrayList())
        return UsernamePasswordAuthenticationToken(principal, token, ArrayList())
    }

    fun getUsername(token: String?): String? {
        return try {
            val jwt = JWT.decode(token)
            val result: Map<String, Claim>
            result = jwt.claims
            //注意!!!这里用toString会多出来引号!!!只能用asString
            result[AUTHORITIES_KEY]!!.asString()
        } catch (e: JWTDecodeException) {
            null
        }
    }

    /**
     * @param token 需要检查的token
     */
    fun checkRenewal(token: String?) {
        // 判断是否续期token,计算token的过期时间
        val time = redisUtils.getExpire(properties.onlineKey + token) * 1000
        val expireDate: Date = DateUtil.offset(Date(), DateField.MILLISECOND, time.toInt())
        // 判断当前时间与过期时间的时间差
        val differ = expireDate.time - System.currentTimeMillis()
        // 如果在续期检查的范围内，则续期
        if (differ <= properties.detect) {
            val renew = time + properties.renew
            redisUtils.expire(properties.onlineKey + token, renew, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 从请求头获取token字段
     * @param request token值
     * @return
     */
    fun getToken(request: HttpServletRequest): String? {
        val requestHeader = request.getHeader(properties.header)
        return if (requestHeader != null && requestHeader.startsWith(properties.tokenStartWith)) {
            requestHeader.substring(7)
        } else null
    }

    companion object {
        const val AUTHORITIES_KEY = "user"
    }
}