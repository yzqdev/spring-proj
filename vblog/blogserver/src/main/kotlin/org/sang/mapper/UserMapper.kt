package org.sang.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.sang.bean.Role
import org.sang.bean.User

/**
 * Created by sang on 2017/12/17.
 */
@Mapper
interface UserMapper {
    fun loadUserByUsername(@Param("username") username: String?): User?
    fun reg(user: User?): Long
    fun updateUserEmail(@Param("email") email: String?, @Param("id") id: Long?): Int
    fun getUserByNickname(@Param("nickname") nickname: String?): List<User?>?
    val allRole: List<Role?>?
    fun updateUserEnabled(@Param("enabled") enabled: Boolean?, @Param("uid") uid: Long?): Int
    fun deleteUserById(uid: Long?): Int
    fun deleteUserRolesByUid(id: Long?): Int
    fun setUserRoles(@Param("rids") rids: Array<Long?>?, @Param("id") id: Long?): Int
    fun getUserById(@Param("id") id: Long?): User?
}