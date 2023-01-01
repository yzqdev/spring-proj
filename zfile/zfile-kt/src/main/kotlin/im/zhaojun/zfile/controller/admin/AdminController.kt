package im.zhaojun.zfile.controller.admin

import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.service.SystemConfigService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

/**
 * 管理后台接口
 * @author zhaojun
 */
@RestController
@RequestMapping("/admin")
class AdminController( private val systemConfigService: SystemConfigService) {


    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    fun config(): ResultBean{

            val systemConfigDTO: SystemConfigDTO = systemConfigService.systemConfig()
            return ResultBean.success(systemConfigDTO)
        }

    /**
     * 更新系统配置
     */
    @PostMapping("/config")
    fun updateConfig(@RequestBody systemConfigDTO: SystemConfigDTO): ResultBean {
        systemConfigDTO.id=1
        val result: SystemConfigDTO = systemConfigService.updateSystemConfig(systemConfigDTO)
        return ResultBean.success(result)
    }

    /**
     * 修改管理员登陆密码
     */
    @PostMapping("/update-pwd")
    fun updatePwd(username: String?, password: String?): ResultBean {
        systemConfigService.updateUsernameAndPwd(username, password)
        return ResultBean.success()
    }
}