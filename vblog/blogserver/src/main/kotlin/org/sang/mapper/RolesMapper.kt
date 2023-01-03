package org.sang.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.sang.bean.Role

/**
 * Created by sang on 2017/12/17.
 */
@Mapper
interface RolesMapper {
    fun addRoles(@Param("roles") roles: Array<String>?, @Param("uid") uid: Long?): Int
    fun getRolesByUid(uid: Long?): List<Role?>?
}