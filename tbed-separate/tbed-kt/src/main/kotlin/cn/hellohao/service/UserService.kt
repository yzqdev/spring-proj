package cn.hellohao.service

import cn.hellohao.model.entity.SysUser
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService

interface UserService : IService<SysUser> {
    //注册
    fun register(sysUser: SysUser): Int

    //登录
    fun login(email: String, password: String, uid: String): Int

    //获取用户信息
    fun getUsers(sysUser: SysUser): SysUser

    //修改资料
    fun change(sysUser: SysUser): Int
    fun changeUser(sysUser: SysUser): Int

    //检查用户名是否重复
    fun checkUsername(username: String): Int
   fun userTotal(): Int
    fun getUserListByName(page: Page<SysUser>, username: String): Page<SysUser>
    fun deleteUserById(id: String): Int

    //查询用户名或者邮箱是否存在
    fun countusername(username: String): Int
    fun countmail(email: String): Int
    fun getUserByUid(uid: String): Int
    fun getUsersMail(uid: String): SysUser
    fun setisok(sysUser: SysUser): Int
    fun setmemory(sysUser: SysUser): Int
    fun getUsersid(id: String): SysUser
    fun usersetmemory(sysUser: SysUser, codestring: String): Int
    fun getuserlistforgroupid(groupid: String): List<SysUser>
}