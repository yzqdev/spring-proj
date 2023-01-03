package org.sang.controller

import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import org.sang.bean.RespBean
import org.sang.bean.User
import org.sang.service.UserService
import org.sang.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sang on 2017/12/17.
 */
@RestController
class LoginRegController {
    @Autowired
    var userService: UserService? = null
    @RequestMapping("/login_error")
    fun loginError(): RespBean {
        return RespBean("error", "登录失败!")
    }

    @RequestMapping("/login_success")
    fun loginSuccess(): RespBean {
        return RespBean("success", "登录成功!")
    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     *
     *
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login_page")
    fun loginPage(): RespBean {
        return RespBean("error", "尚未登录，请登录!")
    }

    @PostMapping("/login")
    fun loginPost(username: String, password: String?): HashMap<String, Any?>? {
        return if (StrUtil.isNotEmpty(username) && StrUtil.isNotEmpty(password)) {
            val queryWrapper = QueryWrapper(
                User(username)
            )
            val adminUser = userService!!.loadUserByUsername(username)
            val token = JwtUtil.sign(username, adminUser!!.authorities.toString())
            val res = HashMap<String, Any?>(1)
            res["token"] = token
            res
        } else {
            null
        }
    }

    @PostMapping("/reg")
    fun reg(user: User): RespBean {
        val result = userService!!.reg(user)
        return if (result == 0) {
            //成功
            RespBean("success", "注册成功!")
        } else if (result == 1) {
            RespBean("error", "用户名重复，注册失败!")
        } else {
            //失败
            RespBean("error", "注册失败!")
        }
    }
}