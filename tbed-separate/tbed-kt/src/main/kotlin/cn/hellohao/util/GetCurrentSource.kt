package cn.hellohao.util

import cn.hellohao.model.entity.SiteGroup
import cn.hellohao.model.entity.SysUser
import cn.hellohao.service.UserGroupService
import cn.hellohao.serviceimport.GroupService
import cn.hellohao.serviceimport.UserService
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 14:10
 */
@Component
class GetCurrentSource(private val groupServiceImpl: GroupService, private val userGroupServiceImpl: UserGroupService, private val userServiceImpl: UserService) {

    @PostConstruct
    fun init() {
        GetCurrentSource.Companion.groupService = groupServiceImpl
        GetCurrentSource.Companion.userGroupService = userGroupServiceImpl
        GetCurrentSource.Companion.userService = userServiceImpl
    }

    companion object {
        private val groupService: GroupService? = null
        private val userGroupService: UserGroupService? = null
        private val userService: UserService? = null
        fun GetSource(userid: String?): SiteGroup? {
            //UserType 0-未分配 1-游客 2-用户 3-管理员
            var sysUser: SysUser? = null
            if (userid != null) {
                val u = SysUser()
                u.id = userid
                sysUser = GetCurrentSource.Companion.userService.getUsers(u)
            }
            var siteGroup: SiteGroup? = null
            siteGroup = if (sysUser == null) {
                //游客
                val count: Int = GetCurrentSource.Companion.groupService.getCountFroUserType(1)
                if (count > 0) {
                    GetCurrentSource.Companion.groupService.getGroupFroUserType(1)
                } else {
                    GetCurrentSource.Companion.groupService.getGroupListById("1")
                }
            } else {
                //用户
                if (sysUser.groupId !== "1") {
                    //说明自定义过的优先
                    GetCurrentSource.Companion.groupService.getGroupListById(sysUser.groupId)
                } else {
                    //默认的，用的是group主键为1的  但是还需要看看用户组有没有设置，比如管理员 用户
                    if (sysUser.level > 1) {
                        //先查询管理员用户组有没有 如果有就用 没有就默认
                        val count: Int = GetCurrentSource.Companion.groupService.getCountFroUserType(3)
                        if (count > 0) {
                            GetCurrentSource.Companion.groupService.getGroupFroUserType(3)
                        } else {
                            GetCurrentSource.Companion.groupService.getGroupListById("1")
                        }
                    } else {
                        //先查询普通用户组有没有 如果有就用 没有就默认
                        val count: Int = GetCurrentSource.Companion.groupService.getCountFroUserType(2)
                        if (count > 0) {
                            GetCurrentSource.Companion.groupService.getGroupFroUserType(2)
                        } else {
                            GetCurrentSource.Companion.groupService.getGroupListById("1")
                        }
                    }
                }
            }
            return siteGroup
        }
    }
}