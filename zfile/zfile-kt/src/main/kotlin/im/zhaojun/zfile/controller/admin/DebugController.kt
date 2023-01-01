package im.zhaojun.zfile.controller.admin

import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.service.SystemConfigService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.annotation.Resource

@Controller
class DebugController {
    @Value("\${zfile.debug}")
    private val debug: Boolean? = null

    @Resource
    private val systemConfigService: SystemConfigService? = null
    @ResponseBody
    @GetMapping("/debug/resetPwd")
    fun resetPwd(): ResultBean {
        return if (debug!!) {
            systemConfigService.updateUsernameAndPwd("admin", "123456")
            ResultBean.success()
        } else {
            ResultBean.error("未开启 DEBUG 模式，不允许进行此操作。")
        }
    }
}