package cn.hellohao.mapper

import cn.hellohao.model.entity.SysUser
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface UserMapper : BaseMapper<SysUser> {
    //注册
    fun register(sysUser: SysUser): Int

    //登录
    fun login(@Param("email") email: String, @Param("password") password: String, @Param("uid") uid: String): Int

    //获取用户信息
    fun getUsers(sysUser: SysUser): SysUser

    //修改资料
    fun change(sysUser: SysUser): Int
    fun changeUser(sysUser: SysUser): Int

    /**
     * 检查用户名是否重复
     *
     * @param username 用户名
     * @return [Integer]
     */
    fun checkUsername(@Param("username") username: String): Int
    fun userTotal(): Int
    fun getUserList(@Param("page") page: Page<SysUser>, username: String): Page<SysUser>

    /**
     * 刪除用戶
     *
     * @param id id
     * @return [Integer]
     */
    fun deleuser(@Param("id") id: String): Int

    /**
     * 查询用户名或者邮箱是否存在
     *
     * @param username 用户名
     * @return [Integer]
     */
    fun countusername(@Param("username") username: String): Int
    fun countmail(@Param("email") email: String): Int

    /**
     * 获取用户uid
     *
     * @param uid uid
     * @return [Integer]
     */
    fun getUserByUid(@Param("uid") uid: String): Int
    fun getUsersMail(@Param("uid") uid: String): SysUser
    fun setIsok(sysUser: SysUser): Int
    fun setmemory(sysUser: SysUser): Int

    /**
     * 得到用户id
     *
     * @param id id
     * @return [SysUser]
     */
    fun getUserById(@Param("id") id: String): SysUser
    fun getUserListFromGroupId(@Param("groupid") groupid: String): List<SysUser>
}