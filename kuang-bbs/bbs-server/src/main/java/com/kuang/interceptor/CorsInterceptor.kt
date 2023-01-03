package com.kuang.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author yanni
 * @date time 2021/11/22 14:28
 * @modified By:
 */
@Component
class CorsInterceptor : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "*")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, X-Requested-With, Content-Type, Accept"
        )
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView
    ) {
        super@HandlerInterceptor.postHandle(request, response, handler, modelAndView)
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception
    ) {
        super@HandlerInterceptor.afterCompletion(request, response, handler, ex)
    }
}