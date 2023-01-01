package cn.hellohao.service.impl

import cn.hellohao.mapper.UserGroupMapper
import cn.hellohao.model.entity.UserGroup
import cn.hellohao.service.UserGroupService
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 14:13
 */
@Service
class UserGroupServiceImpl(private val userGroupMapper: UserGroupMapper) : UserGroupService {

    override fun useridgetusergroup(userid: Int?): UserGroup? {
        return userGroupMapper!!.getUserGroupByUserId(userid)
    }

    override fun idgetusergroup(id: Int?): UserGroup? {
        return userGroupMapper!!.getUserGroupById(id)
    }

    override fun addusergroup(userGroup: UserGroup?): Int? {
        return userGroupMapper!!.addUserGroup(userGroup)
    }

    override fun updateusergroup(userGroup: UserGroup?): Int? {
        return userGroupMapper!!.updateUserGroup(userGroup)
    }

    override fun updateusergroupdefault(groupid: Int?): Int? {
        return userGroupMapper!!.setDefaultUserGroup(groupid)
    }

    override fun deleusergroup(userid: Int?): Int? {
        return userGroupMapper!!.delUserGroup(userid)
    }
}