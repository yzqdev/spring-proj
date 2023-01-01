package im.zhaojun.zfile.controller.install

import cn.hutool.crypto.SecureUtil
import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.service.SystemConfigService
import org.aspectj.apache.bcel.generic.RET
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

/**
 * 系统安装初始化
 * @author zhaojun
 */
@RestController
class InstallController( private val systemConfigService: SystemConfigService) {


    @GetMapping("/is-installed")
   fun isInstall(): ResultBean {
        return if (StringUtils.hasText(systemConfigService?.adminUsername)) {
            ResultBean.error("请勿重复初始化")
        } else {
            ResultBean.success()
        }
    }

    @PostMapping("/doInstall")
    fun install(systemConfigDTO: SystemConfigDTO): ResultBean {
        if (!StringUtils.isEmpty(systemConfigService?.adminUsername)) {
            return ResultBean.error("请勿重复初始化.")
        }
        systemConfigDTO.password=(SecureUtil.md5(systemConfigDTO.password))
        systemConfigService.updateSystemConfig(systemConfigDTO)
        return ResultBean.success()
    }
}