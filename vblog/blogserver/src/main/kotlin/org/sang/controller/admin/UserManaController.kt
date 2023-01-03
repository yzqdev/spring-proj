package org.sang.controller.admin

import org.sang.bean.RespBean
import org.sang.bean.Role
import org.sang.bean.User
import org.sang.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sang on 2017/12/24.
 */
@RestController
@RequestMapping("/admin")
class UserManaController {
    @Autowired
    var userService: UserService? = null
    @RequestMapping(value = ["/user"], method = [RequestMethod.GET])
    fun getUserByNickname(nickname: String?): List<User?>? {
        return userService!!.getUserByNickname(nickname)
    }

    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.GET])
    fun getUserById(@PathVariable id: Long?): User? {
        return userService!!.getUserById(id)
    }

    @get:RequestMapping(value = ["/roles"], method = [RequestMethod.GET])
    val allRole: List<Role?>?
        get() = userService.getAllRole()

    @RequestMapping(value = ["/user/enabled"], method = [RequestMethod.PUT])
    fun updateUserEnabled(enabled: Boolean?, uid: Long?): RespBean {
        return if (userService!!.updateUserEnabled(enabled, uid) == 1) {
            RespBean("success", "更新成功!")
        } else {
            RespBean("error", "更新失败!")
        }
    }

    @RequestMapping(value = ["/user/{uid}"], method = [RequestMethod.DELETE])
    fun deleteUserById(@PathVariable uid: Long?): RespBean {
        return if (userService!!.deleteUserById(uid) == 1) {
            RespBean("success", "删除成功!")
        } else {
            RespBean("error", "删除失败!")
        }
    }

    @RequestMapping(value = ["/user/role"], method = [RequestMethod.PUT])
    fun updateUserRoles(rids: Array<Long?>, id: Long?): RespBean {
        return if (userService!!.updateUserRoles(rids, id) == rids.size) {
            RespBean("success", "更新成功!")
        } else {
            RespBean("error", "更新失败!")
        }
    }
}