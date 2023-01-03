package com.kuang.interceptor

import com.kuang.service.UserService
import com.kuang.utils.JwtUtil.getUserId
import com.kuang.utils.JwtUtil.verifyToken
import com.kuang.utils.UserUtil.getUserByUserCode
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author yanni
 * @date time 2021/11/22 14:18
 * @modified By:
 */
@Component
@Slf4j
class LoginInterceptor : HandlerInterceptor {
    @Resource
    var userService: UserService? = null
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var token = request.getHeader("token")
        if (token == null) {
            token = request.getParameter("token")
        }
        LoginInterceptor.log.info("token=$token")
        val flag = verifyToken(token)
        if (flag) {
            val userId = getUserId(token)!!
            val user = getUserByUserCode(userId)
            val level = 0
            request.setAttribute("user", user)
            // String email = (String) session.getAttribute("email");
            //如果session中没有user，表示没登陆
            println("-----------------------------------------------------------")
            println(user)
            return if (user.getUsername() == null) {
                //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
                //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
                println("没有登录权限")
                false
            } else {
                if (level == 2) {
                    println("进入成功")
                    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
                    true
                } else {
                    false
                }
            }
        }
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView
    ) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception
    ) {
    }
}