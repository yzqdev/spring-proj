package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.kuang.model.entity.*
import com.kuang.model.vo.RegisterForm
import com.kuang.service.InviteService
import com.kuang.service.UserInfoService
import com.kuang.service.UserService
import com.kuang.utils.JwtUtil.sign
import com.kuang.utils.KuangUtils.print
import com.kuang.utils.KuangUtils.time
import com.kuang.utils.RequestHelper.sessionUser
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

/**
 * 登录控制器
 *
 * @author yanni
 * @date 2021/11/22
 */
@RestController
@RequestMapping
class LoginController {
    @Resource
    var inviteService: InviteService? = null

    @Resource
    var userService: UserService? = null

    @Resource
    var userInfoService: UserInfoService? = null

    @get:GetMapping("/getUser")
    val user: User
        get() = sessionUser

    @Parameters(
        Parameter(name = "username", description = "名字", required = true, example = "yzq"),
        Parameter(name = "password", description = "密码", required = true, example = "123")
    )
    @PostMapping("/login")
    fun login(@RequestParam("username") username: String?, @RequestParam("password") password: String?): String {
        val sqlUser = userService!!.getOne(LambdaQueryWrapper<User>().eq(User::getUsername, username))
        return if (sqlUser.getUsername() != null) {
            if (password == sqlUser.getPassword()) {
                sign(sqlUser.getUsername(), sqlUser.getUserId())
            } else {
                "密码错误"
            }
        } else {
            "用户不存在"
        }
    }

    /**
     * 注册
     * 注册业务
     *
     * @param registerForm 注册表单
     * @return [String]
     */
    @PostMapping("/register")
    fun register(@RequestBody registerForm: RegisterForm): HashMap<String, Any> {
        print("注册表单信息：$registerForm")
        // 表单密码重复判断
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        if (!registerForm.getPassword().equals(registerForm.getRepassword())) {
            model["registerMsg"] = "密码输入有误"
            return model
        }
        // 用户名已存在
        val hasUser = userService!!.getOne(QueryWrapper<User>().eq("username", registerForm.getUsername()))
        if (hasUser != null) {
            model["registerMsg"] = "用户名已存在"
            return model
        }

        // 验证邀请码
        val invite = inviteService!!.getOne(QueryWrapper<Invite>().eq("code", registerForm.getCode()))
        return if (invite == null) {
            model["registerMsg"] = "邀请码不存在"
            model
        } else {
            // 邀请码存在，判断邀请码是否有效
            if (invite.getStatus() === 1) {
                model["registerMsg"] = "邀请码已被使用"
                model
            } else {
                // 构建用户对象
                val user = User()
                user.setUserId("aaa")
                //user.setUserId(KuangUtils.getUuid()); // 用户唯一id
                user.setRoleId(2)
                user.setUsername(registerForm.getUsername())
                // 密码加密
                user.setPassword(registerForm.getPassword())
                user.setGmtCreate(time)
                user.setLoginDate(time)
                // 保存对象！
                userService!!.save(user)
                print("新用户注册成功：$user")

                // 激活邀请码
                invite.setActiveTime(time)
                invite.setStatus(1)
                invite.setUid(user.getUserId())
                inviteService!!.updateById(invite)

                // todo: 用户信息
                userInfoService!!.save(UserInfo().setUid(user.getUserId()))

                // 注册成功，重定向到登录页面
                model
            }
        }
    }
}