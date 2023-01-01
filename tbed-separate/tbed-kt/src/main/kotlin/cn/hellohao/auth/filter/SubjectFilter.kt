package cn.hellohao.auth.filter

import cn.hellohao.auth.token.JWTUtil
import cn.hellohao.auth.token.UserClaim
import cn.hellohao.model.entity.SysUser
import cn.hellohao.serviceimport.UserService
import cn.hellohao.util.SpringContextHolder
import cn.hutool.core.lang.Console
import com.alibaba.fastjson.JSONObject
import lombok.extern.slf4j.Slf4j
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/16 17:43
 */
@Slf4j
class SubjectFilter : BasicHttpAuthenticationFilter() {
    private var CODE = "000"

    /**
     * 是访问允许
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 映射值
     * @return boolean
     */
    protected override fun isAccessAllowed(
        request: ServletRequest,
        response: ServletResponse,
        mappedValue: Any
    ): Boolean {
        val userService: UserService = SpringContextHolder.getBean(UserService::class.java)
        val httpServletRequest: HttpServletRequest = request as HttpServletRequest
        val httpServletResponse: HttpServletResponse = response as HttpServletResponse
        val serviceName: String = httpServletRequest.getServletPath()
        val Users_Origin: String = httpServletRequest.getHeader("usersOrigin")

        //验证前端域名
        if (httpServletRequest.getMethod() == "POST" && !serviceName.contains("/api") && !serviceName.contains("/verifyCode")) {
            try {

                //todo 这里禁止其他网站使用我们的api
                //for (String item:  WEBHOST) {
                //    if(Users_Origin.compareTo(SecureUtil.md5(item))!=0){
                //        System.out.println("前端域名校验未通过");
                //        System.out.println("request-MD5:"+Users_Origin);
                //        System.out.println("配置文件-MD5:"+SecureUtil.md5(item));
                //        System.out.println("配置Host:"+item);
                //        this.CODE = "406";
                //        return false;
                //    }
                //}
            } catch (e: Exception) {
                e.printStackTrace()
                CODE = "500"
                return false
            }
        }
        Console.log("service-name=>{}", serviceName)
        Console.print("http=>{}", httpServletRequest.getParameter("username"))
        if (serviceName.contains("/user/activation")) {
            return true
        }
        val token: String = httpServletRequest.getHeader("Authorization")
        Console.error("从filter获取token {}", token)
        val jsonObject: UserClaim = JWTUtil.checkToken(token)
        Console.error(jsonObject.toString())
        if (java.lang.Boolean.FALSE == jsonObject.check) {
            return if (!serviceName.contains("admin")) {
                true
            } else {
                CODE = "403"
                false
            }
        } else {
            val subject = SecurityUtils.getSubject()
            val sysUser: SysUser = subject.principal as SysUser
            if (sysUser == null) {
                val tokenOBJ = UsernamePasswordToken(jsonObject.email, jsonObject.password)
                tokenOBJ.setRememberMe(true)
                try {
                    subject.login(tokenOBJ)
                    //一小时
                    SecurityUtils.getSubject().session.timeout = 3600000
                } catch (e: Exception) {
                    println("拦截器，登录失败")
                    CODE = "403"
                    return false
                }
            } else {
                if (null != sysUser) {
                    try {
                        if (null != sysUser.id) {
                            if (userService.getUsers(sysUser).isok!! < 1) {
                                subject.logout()
                                CODE = "403"
                                return false
                            }
                        }
                    } catch (e: Exception) {
                        Console.log("拦截器判断用户isOK的时候报错了")
                        e.printStackTrace()
                    }
                }
            }
        }
        return true
    }

    /**
     * 在访问被拒绝
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 映射值
     * @return boolean
     */
    protected override fun onAccessDenied(
        request: ServletRequest,
        response: ServletResponse,
        mappedValue: Any
    ): Boolean {
        var info = "未知错误"
        try {
            info = when (CODE) {
                "406" -> "前端域名配置不正确"
                "403" -> "当前用户无权访问该请求"
                "402" -> "当前web请求不合规"
                else -> "default"
            }
            Console.log("拦截器False-$info")
            response.setContentType("application/json;charset=UTF-8")
            val jsonObject = JSONObject()
            jsonObject["code"] = CODE
            jsonObject["info"] = info
            response.getWriter().write(jsonObject.toJSONString())
        } catch (e: Exception) {
            Console.log("返回token验证失败403请求，报异常了")
        }
        return false
    }

    companion object {
        var WEBHOST: Array<String>? = null
    }
}