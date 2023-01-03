package org.sang.config

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by sang on 2017/12/22.
 */
class AuthenticationAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(httpServletRequest: HttpServletRequest, resp: HttpServletResponse, e: AccessDeniedException) {
        resp.status = HttpServletResponse.SC_FORBIDDEN
        resp.characterEncoding = "UTF-8"
        val out = resp.writer
        out.write("权限不足,请联系管理员!")
        out.flush()
        out.close()
    }
}