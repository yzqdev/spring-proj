package cn.hellohao.service

import cn.hellohao.model.entity.UserGroup
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 14:12
 */
@Service
interface UserGroupService {
    fun useridgetusergroup(userid: Int?): UserGroup?
    fun idgetusergroup(id: Int?): UserGroup?
    fun addusergroup(userGroup: UserGroup?): Int?
    fun updateusergroup(userGroup: UserGroup?): Int?
    fun updateusergroupdefault(groupid: Int?): Int?
    fun deleusergroup(userid: Int?): Int?
}