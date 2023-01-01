package im.zhaojun.zfile.controller.admin

import im.zhaojun.zfile.context.StorageTypeContext
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.model.support.ResultBean.Companion.successData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 系统元数据 Controller
 * @author zhaojun
 */
@RestController
@RequestMapping("/admin")
class MateDataController {
    /**
     * 返回支持的存储引擎.
     */
    @GetMapping("/support-strategy")
    fun supportStrategy(): ResultBean {
        val values = StorageTypeEnum.values()
        return successData(values)
    }

    /**
     * 获取指定存储策略的表单域
     *
     * @param   storageType
     * 存储策略
     *
     * @return  所有表单域
     */
    @GetMapping("/strategy-form")
    fun getFormByStorageType(storageType: StorageTypeEnum?): ResultBean {
        val storageTypeService = StorageTypeContext.getStorageTypeService(storageType)
        val storageConfigList: List<StorageConfig> = storageTypeService.storageStrategyConfigList()
        return ResultBean.success(storageConfigList)
    }
}