package cn.hellohao.service.impl

import cn.hellohao.exception.CodeException
import cn.hellohao.mapper.GroupMapper
import cn.hellohao.mapper.UserMapper
import cn.hellohao.model.entity.Msg
import cn.hellohao.model.entity.SiteGroup
import cn.hellohao.model.entity.SysUser
import cn.hellohao.service.GroupService
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:30
 */
@Service
class GroupServiceImpl(    private val groupMapper: GroupMapper, private val userMapper: UserMapper) : ServiceImpl<GroupMapper, SiteGroup>(),
    GroupService {

    override fun groupList(page: Page<SiteGroup>, usertype: Int): Page<SiteGroup> {
        return groupMapper!!.grouplist(page, usertype)
    }

    override fun getGroupListById(id: String): SiteGroup {
        return groupMapper!!.idGroupList(id)
    }

    override fun addGroup(siteGroup: SiteGroup): Msg {
        val msg = Msg()
        if (siteGroup.userType != 0) {
            val count = groupMapper!!.getUserTypeCount(siteGroup.userType)
            if (count == 0) {
                groupMapper.addgroup(siteGroup)
                msg.info = "添加成功"
            } else {
                msg.code = "110401"
                msg.info = "分配的该用户组已存在。请勿重复分配。"
            }
        } else {
            groupMapper!!.addgroup(siteGroup)
            msg.info = "添加成功"
        }
        return msg
    }

    override fun getCountFroUserType(usertype: Int): Int {
        return groupMapper!!.getUserTypeCount(usertype)
    }

    @Transactional //默认遇到throw new RuntimeException(“…”);会回滚
    override fun deleteGroup(id: String): Msg {
        val msg = Msg()
        var ret = 0
        ret = groupMapper!!.deleteGroup(id)
        if (ret > 0) {
            val sysUserList = userMapper!!.getUserListFromGroupId(id)
            for (sysUser in sysUserList) {
                val u = SysUser()
                u.groupId = "1"
                u.uid = sysUser.uid
                userMapper.change(u)
            }
            msg.info = "删除成功"
        } else {
            msg.code = "500"
            msg.info = "删除成功"
            throw CodeException("用户之没有设置成功。")
        }
        return msg
    }

    override fun setGroup(siteGroup: SiteGroup): Msg {
        val msg = Msg()
        if (siteGroup.userType != 0) {
            val siteGroupFroUserType = groupMapper!!.getGroupByUserType(siteGroup.userType)
            if (siteGroupFroUserType != null) {
                if (siteGroupFroUserType.userType == siteGroup.userType) {
                    if (siteGroupFroUserType.id == siteGroup.id) {
                        groupMapper.updateById(siteGroup)
                        msg.info = "修改成功"
                    } else {
                        msg.code = "110401"
                        msg.info = "分配的该用户组已存在。请勿重复分配。"
                    }
                } else {
                    if (groupMapper.getUserTypeCount(siteGroup.userType) > 0) {
                        msg.code = "110401"
                        msg.info = "分配的该用户组已存在。请勿重复分配。"
                    } else {
                        groupMapper.updateById(siteGroup)
                        msg.info = "修改成功"
                    }
                }
            } else {
                groupMapper.updateById(siteGroup)
                msg.info = "修改成功"
            }
        } else {
            groupMapper!!.updateById(siteGroup)
            msg.info = "修改成功"
        }
        return msg
    }

    override fun getGroupFroUserType(usertype: Int): SiteGroup {
        return groupMapper!!.getGroupByUserType(usertype)
    }
}