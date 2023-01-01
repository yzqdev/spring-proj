package im.zhaojun.zfile.controller.admin

import com.alibaba.fastjson.JSONObject
import im.zhaojun.zfile.model.dto.DriveConfigDTO
import im.zhaojun.zfile.model.entity.FilterConfig
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.model.support.ResultBean.Companion.error
import im.zhaojun.zfile.service.DriveConfigService
import im.zhaojun.zfile.service.FilterConfigService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

/**
 * 驱动器相关操作 Controller
 * @author zhaojun
 */
@RestController
@RequestMapping("/admin")
class DriveController {
    @Resource
    private val driveConfigService: DriveConfigService? = null

    @Resource
    private val filterConfigService: FilterConfigService? = null

    /**
     * 获取所有驱动器列表
     *
     * @return  驱动器列表
     */
    @GetMapping("/drives")
    fun driveList(): ResultBean {
        val list = driveConfigService!!.list()
        return ResultBean.success(list)
    }

    /**
     * 获取指定驱动器基本信息及其参数
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  驱动器基本信息
     */
    @GetMapping("/drive/{driveId}")
    fun driveItem(@PathVariable driveId: String?): ResultBean {
        val driveConfig = driveConfigService!!.findDriveConfigDTOById(driveId!!)
        return ResultBean.success(driveConfig)
    }

    /**
     * 保存驱动器设置
     */
    @PostMapping("/drive")
    fun saveDriveItem(@RequestBody driveConfigDTO: DriveConfigDTO?): ResultBean {
        driveConfigService!!.saveDriveConfigDTO(driveConfigDTO!!)
        return ResultBean.success()
    }

    /**
     * 删除驱动器设置
     *
     * @param   driveId
     * 驱动器 ID
     */
    @DeleteMapping("/drive/{driveId}")
    fun deleteDriveItem(@PathVariable driveId: String?): ResultBean {
        driveConfigService!!.deleteById(driveId!!)
        return ResultBean.success()
    }

    /**
     * 启用驱动器
     *
     * @param   driveId
     * 驱动器 ID
     */
    @PostMapping("/drive/{driveId}/enable")
    fun enable(@PathVariable driveId: String?): ResultBean {
        val driveConfig = driveConfigService!!.findById(driveId!!)
        driveConfig.enable=true
        driveConfigService.updateDriveConfig(driveConfig)
        return ResultBean.success()
    }

    /**
     * 停止驱动器
     *
     * @param   driveId
     * 驱动器 ID
     */
    @PostMapping("/drive/{driveId}/disable")
    fun disable(@PathVariable driveId: String?): ResultBean {
        val driveConfig = driveConfigService!!.findById(driveId!!)
        driveConfig.enable=false
        driveConfigService.updateDriveConfig(driveConfig)
        return ResultBean.success()
    }

    /**
     * 根据驱动器 ID 获取过滤文件列表
     *
     * @param   driveId
     * 驱动器 ID
     */
    @GetMapping("/drive/{driveId}/filters")
    fun getFilters(@PathVariable driveId: String?): ResultBean {
        return ResultBean.success(filterConfigService!!.findByDriveId(driveId))
    }

    /**
     * 停止驱动器
     *
     * @param   driveId
     * 驱动器 ID
     */
    @PostMapping("/drive/{driveId}/filters")
    fun saveFilters(@RequestBody filter: List<FilterConfig?>?, @PathVariable driveId: String?): ResultBean {
        filterConfigService!!.batchSave(filter!!, driveId)
        return ResultBean.success()
    }

    /**
     * 保存拖拽排序信息
     *
     * @param   driveConfigs
     * 拖拽排序信息
     */
    @PostMapping("/drive/drag")
    fun saveDriveDrag(@RequestBody driveConfigs: List<JSONObject>): ResultBean {
        driveConfigService!!.saveDriveDrag(driveConfigs)
        return ResultBean.success()
    }

    /**
     * 更新驱动器 ID
     *
     * @param   updateId
     * 驱动器原 ID
     *
     * @param   newId
     * 驱动器新 ID
     */
    @PostMapping("/drive/updateId")
    fun updateDriveId(updateId: String?, newId: String?): ResultBean {
        val driveConfig = driveConfigService!!.findById(newId!!)
        if (driveConfig != null) {
            return error("已存在的 ID，请更换 ID 后重试。")
        }
        driveConfigService.updateId(updateId!!, newId)
        return ResultBean.success()
    }
}