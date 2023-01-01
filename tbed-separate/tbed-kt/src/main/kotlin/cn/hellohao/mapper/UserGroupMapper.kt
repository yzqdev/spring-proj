package cn.hellohao.mapper

import cn.hellohao.model.entity.UserGroup
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 13:45
 */
@Mapper
interface UserGroupMapper : BaseMapper<UserGroup?> {
    /**
     * 用户组用户id
     *
     * @param userid 用户标识
     * @return [UserGroup]
     */
    fun getUserGroupByUserId(@Param("userid") userid: Int?): UserGroup?

    /**
     * 得到用户组id
     *
     * @param id id
     * @return [UserGroup]
     */
    fun getUserGroupById(@Param("id") id: Int?): UserGroup?

    /**
     * 添加用户组
     *
     * @param userGroup 用户组
     * @return [Integer]
     */
    fun addUserGroup(userGroup: UserGroup?): Int?

    /**
     * 更新用户组
     *
     * @param userGroup 用户组
     * @return [Integer]
     */
    fun updateUserGroup(userGroup: UserGroup?): Int?

    /**
     * 设置默认用户组
     *
     * @param groupid groupid
     * @return [Integer]
     */
    fun setDefaultUserGroup(@Param("groupid") groupid: Int?): Int?

    /**
     * 德尔用户组
     *
     * @param userid 用户标识
     * @return [Integer]
     */
    fun delUserGroup(@Param("userid") userid: Int?): Int?
}