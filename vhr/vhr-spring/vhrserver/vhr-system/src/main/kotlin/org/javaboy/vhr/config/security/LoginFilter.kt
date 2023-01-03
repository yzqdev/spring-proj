package org.javaboy.vhr.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.javaboy.vhr.constant.Constants
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import java.io.IOException
import java.util.*

/**
 * @author yanni
 */
@Slf4j
class LoginFilter : UsernamePasswordAuthenticationFilter() {
    @Autowired
    var sessionRegistry: SessionRegistry? = null
    @Throws(AuthenticationException::class)
    fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        if ("POST" != request.getMethod()) {
            throw AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod()
            )
        }
        val session: HttpSession = request.getSession()
        // 获取session中所有的键值
        val verifyCode = request.getSession().getAttribute(Constants.Companion.VERIFY_CODE) as String
        LoginFilter.log.info("LoginFilter验证码：$verifyCode")
        LoginFilter.log.info(obtainUsername(request))
        return if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) || request.getContentType()
                .contains(
                    MediaType.APPLICATION_JSON_VALUE
                )
        ) {
            var loginData: Map<String?, String?> = HashMap()
            try {
                loginData = ObjectMapper().readValue<Map<*, *>>(request.getInputStream(), MutableMap::class.java)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                val code = loginData["code"]
                checkCode(response, code, verifyCode)
            }
            var username = loginData[getUsernameParameter()]
            var password = loginData[getPasswordParameter()]
            if (username == null) {
                username = ""
            }
            if (password == null) {
                password = ""
            }
            username = username.trim { it <= ' ' }
            val authRequest = UsernamePasswordAuthenticationToken(
                username, password
            )
            setDetails(request, authRequest)
            val principal = Hr()
            principal.setUsername(username)
            //sessionRegistry.registerNewSession(request.getSession(true).getId(), principal);
            this.getAuthenticationManager().authenticate(authRequest)
        } else {
            checkCode(response, request.getParameter("code"), verifyCode)
            super.attemptAuthentication(request, response)
        }
    }

    fun checkCode(resp: HttpServletResponse?, code: String?, verify_code: String?) {
        if (code == null || verify_code == null || "" == code || !verify_code.equals(
                code.lowercase(Locale.getDefault()),
                ignoreCase = true
            )
        ) {
            //验证码不正确
            throw AuthenticationServiceException("验证码不正确")
        }
    }
}