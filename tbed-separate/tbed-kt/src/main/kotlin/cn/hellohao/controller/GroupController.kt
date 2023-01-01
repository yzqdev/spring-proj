package cn.hellohao.controller

import cn.hellohao.model.dto.PageDto
import cn.hellohao.model.entity.Msg
import cn.hellohao.model.entity.SiteGroup
import cn.hellohao.service.GroupService
import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:35
 */
@Controller
@RequestMapping("/admin/root")
class GroupController( private val groupService: GroupService) {

    @PostMapping("/getGrouplistForUsers")
    @ResponseBody

    fun grouplistForUsers(): Msg
    {
            val msg = Msg()
            val siteGroupList: Page<SiteGroup> = groupService.groupList(Page(0, 20), 0)
            msg.data = siteGroupList.getRecords()
            return msg
        }

    @PostMapping(value = ["/getGroupList"]) //new
    @ResponseBody
    fun getgrouplist(@RequestBody data: PageDto): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val pageNum: Int = data.pageNum
        val pageSize: Int = data.pageSize
        val page: Page<SiteGroup> = Page(data.pageNum.toLong(), data.pageSize.toLong())
        //todo 添加分页
        var rolePageInfo: Page<SiteGroup>
        try {
            rolePageInfo = groupService.groupList(page, null)
            map["code"] = 200
            map["info"] = ""
            map["count"] = rolePageInfo.getTotal()
            map["data"] = rolePageInfo.getRecords()
        } catch (e: Exception) {
            e.printStackTrace()
            map["code"] = 500
            map["info"] = "获取数据异常"
        }
        return map
    }

    @PostMapping(value = ["/addGroup"]) //new
    @ResponseBody
    fun addisgroup(@RequestParam(value = "data", defaultValue = "") data: String?): Msg {
        val jsonObject = JSONObject.parseObject(data)
        val siteGroup = SiteGroup().apply {
            groupName=jsonObject.getString("groupname")
            keyID=jsonObject.getString("keyid")
            userType=jsonObject.getInteger("usertype")
            compress=if (jsonObject.getBoolean("compress")) 1 else 0
        }

        return groupService.addGroup(siteGroup)
    }

    @PostMapping("/updateGroup") //new
    @ResponseBody
    fun updategroup(@RequestBody siteGroup: SiteGroup): Msg {
        if (siteGroup.id=== "1") {
            siteGroup.groupName=("默认群组")
            siteGroup.userType=0
        } else {
            //siteGroup.setGroupName(jsonObject.getString("groupname"));
            //siteGroup.setUserType(jsonObject.getInteger("usertype"));
        }
        println("updateGroup")
        System.out.println(siteGroup.compress)
        if (siteGroup.compress.toString() === "true") {
            siteGroup.compress=1
        } else {
            siteGroup.compress=0
        }
        return groupService.setGroup(siteGroup)
    }

    @PostMapping(value = ["/deleGroup"]) //new
    @ResponseBody
    fun delegroup(@RequestParam(value = "data", defaultValue = "") data: String?): Msg? {
        val jsonObject = JSONObject.parseObject(data)
        val id = jsonObject.getString("id")
        var msg: Msg? = null
        return if (id !== "1") {
            msg = groupService.deleteGroup(id)
            msg
        } else {
            val msg2 = Msg()
            msg2.code = "500"
            msg2.info = "默认群组不可删除"
            msg2
        }
    }
}