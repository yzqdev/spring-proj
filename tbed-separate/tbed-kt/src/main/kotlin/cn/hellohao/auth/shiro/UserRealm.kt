package cn.hellohao.auth.shiro

import cn.hellohao.model.entity.SysUser
import cn.hellohao.serviceimport.UserService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection

/**
 * @author Hellohao
 * @version 1.0
 * @date  2021/6/3 10:39
 * 自定义UserRealm
 */
class UserRealm(private val userService: UserService) : AuthorizingRealm() {

    protected override fun doGetAuthorizationInfo(principalCollection: PrincipalCollection): AuthorizationInfo {
        val info = SimpleAuthorizationInfo()
        val subject = SecurityUtils.getSubject()
        val sysUser: SysUser = subject.principal as SysUser
        val roleList: ArrayList<String?> = arrayListOf()
        if (sysUser.level=== 2) {
            roleList.add("admin")
            roleList.add("sysUser")
        } else {
            roleList.add("sysUser")
        }
        info.addRoles(roleList)
        return info
    }

    @Throws(AuthenticationException::class)
    protected override fun doGetAuthenticationInfo(tokenOBJ: AuthenticationToken): AuthenticationInfo {
        val userToken: UsernamePasswordToken
        userToken = tokenOBJ as UsernamePasswordToken
        val sysUser = SysUser()
        sysUser.email=userToken.username
        val u: SysUser = userService.getUsers(sysUser)
        //密码认证（防止泄露，不需要我们做）
        return SimpleAuthenticationInfo(u, u.password, "")
    }
}