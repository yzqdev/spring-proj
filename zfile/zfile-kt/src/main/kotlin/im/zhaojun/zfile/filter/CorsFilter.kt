package im.zhaojun.zfile.filter

import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 开启跨域支持. 一般用于开发环境, 或前后端分离部署时开启.
 * @author zhaojun
 */
@Order(1)
@WebFilter(value = ["/*"])
@Component
class CorsFilter : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.setHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
            httpServletRequest.getHeader(HttpHeaders.ORIGIN)
        )
        httpServletResponse.setHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
            "Origin, X-Requested-With, Content-Type, Accept"
        )
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600")
        if (!CorsUtils.isPreFlightRequest(httpServletRequest)) {
            chain.doFilter(httpServletRequest, httpServletResponse)
        }
    }
}