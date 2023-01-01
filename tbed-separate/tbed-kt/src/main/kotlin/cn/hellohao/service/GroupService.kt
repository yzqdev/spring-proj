package cn.hellohao.service

import cn.hellohao.model.entity.Msg
import cn.hellohao.model.entity.SiteGroup
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:29
 */
@Service
interface GroupService : IService<SiteGroup> {
    fun groupList(page: Page<SiteGroup>, usertype: Int?): Page<SiteGroup>
    fun getGroupListById(id: String): SiteGroup
    fun addGroup(siteGroup: SiteGroup): Msg
    fun getCountFroUserType(usertype: Int): Int
    fun deleteGroup(id: String): Msg
    fun setGroup(siteGroup: SiteGroup): Msg
    fun getGroupFroUserType(usertype: Int): SiteGroup
}