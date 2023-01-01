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

import cn.hutool.core.util.StrUtil
import com.auth0.jwt.exceptions.TokenExpiredException
import me.zhengjie.modules.security.config.bean.SecurityProperties
import me.zhengjie.modules.security.service.OnlineUserService
import me.zhengjie.modules.security.service.UserCacheClean
import me.zhengjie.modules.security.service.dto.OnlineUserDto
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * @author /
 */
class TokenFilter
/**
 * @param tokenProvider     Token
 * @param properties        JWT
 * @param onlineUserService 用户在线
 * @param userCacheClean    用户缓存清理工具
 */(
    private val tokenProvider: TokenProvider?,
    private val properties: SecurityProperties?,
    private val onlineUserService: OnlineUserService?,
    private val userCacheClean: UserCacheClean?
) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val token = resolveToken(httpServletRequest)
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            var onlineUserDto: OnlineUserDto? = null
            var cleanUserCache = false
            try {
                onlineUserDto = onlineUserService!!.getOne(properties.getOnlineKey() + token)
            } catch (e: TokenExpiredException) {
                log.error(e.message)
                cleanUserCache = true
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    userCacheClean!!.cleanUserCache(tokenProvider!!.getUsername(token))
                }
            }
            if (onlineUserDto != null && StringUtils.hasText(token)) {
                val authentication = tokenProvider!!.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
                // Token 续期
                tokenProvider.checkRenewal(token)
            }
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(properties.getHeader())
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "")
        } else {
            log.debug("非法Token：{}", bearerToken)
        }
        return null
    }

    companion object {
        private val log = LoggerFactory.getLogger(TokenFilter::class.java)
    }
}