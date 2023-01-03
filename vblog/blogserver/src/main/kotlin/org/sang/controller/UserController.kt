package org.sang.controller

import org.sang.bean.RespBean
import org.sang.service.UserService
import org.sang.utils.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sang on 2017/12/24.
 */
@RestController
class UserController {
    @Autowired
    var userService: UserService? = null
    @RequestMapping("/currentUserName")
    fun currentUserName(): String? {
        return Util.getCurrentUser().nickname
    }

    @RequestMapping("/currentUserId")
    fun currentUserId(): Long? {
        return Util.getCurrentUser().id
    }

    @RequestMapping("/currentUserEmail")
    fun currentUserEmail(): String? {
        return Util.getCurrentUser().email
    }

    @get:RequestMapping("/isAdmin")
    val isAdmin: Boolean
        get() {
            val authorities = Util.getCurrentUser().authorities
            for (authority in authorities!!) {
                if (authority!!.authority.contains("超级管理员")) {
                    return true
                }
            }
            return false
        }

    @RequestMapping(value = ["/updateUserEmail"], method = [RequestMethod.PUT])
    fun updateUserEmail(email: String?): RespBean {
        return if (userService!!.updateUserEmail(email) == 1) {
            RespBean("success", "开启成功!")
        } else RespBean("error", "开启失败!")
    }
}