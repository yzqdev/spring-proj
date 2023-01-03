package org.sang.config

import cn.hutool.core.util.StrUtil
import lombok.extern.slf4j.Slf4j
import org.sang.bean.User
import org.sang.utils.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.util.StringUtils
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author yanni
 */
@Slf4j
class JWTAuthenticationFilter : BasicAuthenticationFilter {
    private var tokenExpireTime: Int? = null

    constructor(authenticationManager: AuthenticationManager?, tokenExpireTime: Int?) : super(authenticationManager) {
        this.tokenExpireTime = tokenExpireTime
    }

    constructor(
        authenticationManager: AuthenticationManager?,
        authenticationEntryPoint: AuthenticationEntryPoint?
    ) : super(authenticationManager, authenticationEntryPoint) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        var token = request.getHeader(SecurityConstant.TOKEN)
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(SecurityConstant.TOKEN)
        }
        val notValid = StrUtil.isBlank(token) || !token.startsWith(SecurityConstant.TOKEN_SPLIT)
        if (notValid) {
            chain.doFilter(request, response)
            return
        }
        try {
            //UsernamePasswordAuthenticationToken 继承 AbstractAuthenticationToken 实现 Authentication
            //所以当在页面中输入用户名和密码之后首先会进入到 UsernamePasswordAuthenticationToken验证(Authentication)，
            val authentication = getAuthentication(token, response)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            e.printStackTrace()
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String, response: HttpServletResponse): UsernamePasswordAuthenticationToken? {
        var username: String? = null
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        try {
            //解析token
            username = JwtUtil.getUserName(token)
            logger.info("username：$username")
            //获取权限
            val authority = JwtUtil.getUserAuth(token)
            logger.info("authority：$authority")
            if (StringUtils.hasText(authority)) {
                authorities.add(SimpleGrantedAuthority(authority))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (StrUtil.isNotBlank(username)) {
            //踩坑提醒 此处password不能为null
            val principal = User(username!!)
            return UsernamePasswordAuthenticationToken(principal, null, authorities)
        }
        return null
    }
}