package org.sang.service

import org.sang.bean.Role
import org.sang.bean.User
import org.sang.mapper.RolesMapper
import org.sang.mapper.UserMapper
import org.sang.utils.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sang on 2017/12/17.
 */
@Service
@Transactional
class UserService : UserDetailsService {
    @Autowired
    var userMapper: UserMapper? = null

    @Autowired
    var rolesMapper: RolesMapper? = null

    @Autowired
    var passwordEncoder: PasswordEncoder? = null
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userMapper!!.loadUserByUsername(s)
            ?: //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            throw UsernameNotFoundException("用户不存在")
        //查询用户的角色信息，并返回存入user中
        val roles = rolesMapper!!.getRolesByUid(user.id)
        user.roles = roles
        return user
    }

    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    fun reg(user: User): Int {
        val loadUserByUsername = userMapper!!.loadUserByUsername(user.username)
        if (loadUserByUsername != null) {
            return 1
        }
        //插入用户,插入之前先对密码进行加密
        user.password = passwordEncoder!!.encode(user.password)
        user.isEnabled = true //用户可用
        val result = userMapper!!.reg(user)
        //配置用户的角色，默认都是普通用户
        val roles = arrayOf("2")
        val i = rolesMapper!!.addRoles(roles, user.id)
        val b = i == roles.size && result == 1L
        return if (b) {
            0
        } else {
            2
        }
    }

    fun updateUserEmail(email: String?): Int {
        return userMapper!!.updateUserEmail(email, Util.getCurrentUser().id)
    }

    fun getUserByNickname(nickname: String?): List<User?>? {
        return userMapper!!.getUserByNickname(nickname)
    }

    val allRole: List<Role?>?
        get() = userMapper.getAllRole()

    fun updateUserEnabled(enabled: Boolean?, uid: Long?): Int {
        return userMapper!!.updateUserEnabled(enabled, uid)
    }

    fun deleteUserById(uid: Long?): Int {
        return userMapper!!.deleteUserById(uid)
    }

    fun updateUserRoles(rids: Array<Long?>?, id: Long?): Int {
        val i = userMapper!!.deleteUserRolesByUid(id)
        return userMapper!!.setUserRoles(rids, id)
    }

    fun getUserById(id: Long?): User? {
        return userMapper!!.getUserById(id)
    }
}