package me.zhengjie.modules.security.security

import lombok.extern.slf4j.Slf4j
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

/**
 * 这个类并没有什么作用,只是测试过滤器和拦截器的区别
 */
@Slf4j
class AuthFilter : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        AuthFilter.log.debug("start to auth request validate...111")
        val req = request as HttpServletRequest
        val token = req.getHeader("Authorization")
        if (token == null) {
            //    :TODO check token
            AuthFilter.log.info("auth success")
        } else {
            AuthFilter.log.error("auth failed")
        }
        chain.doFilter(request, response)
    }
}