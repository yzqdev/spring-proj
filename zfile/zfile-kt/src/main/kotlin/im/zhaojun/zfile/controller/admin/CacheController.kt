package im.zhaojun.zfile.controller.admin

import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.service.DriveConfigService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

/**
 * 缓存 Controller
 *
 * @author zhaojun
 */
@RestController
@RequestMapping("/admin/cache")
class CacheController {
    @Resource
    private val driveConfigService: DriveConfigService? = null
    @PostMapping("/{driveId}/enable")
    fun enableCache(@PathVariable("driveId") driveId: String?): ResultBean {
        driveConfigService!!.updateCacheStatus(driveId!!, true)
        return ResultBean.success()
    }

    @PostMapping("/{driveId}/disable")
    fun disableCache(@PathVariable("driveId") driveId: String?): ResultBean {
        driveConfigService!!.updateCacheStatus(driveId!!, false)
        return ResultBean.success()
    }

    @GetMapping("/{driveId}/info")
    fun cacheInfo(@PathVariable("driveId") driveId: String?): ResultBean {
        val cacheInfo = driveConfigService!!.findCacheInfo(driveId)
        return ResultBean.success(cacheInfo)
    }

    @PostMapping("/{driveId}/refresh")
    @Throws(Exception::class)
    fun refreshCache(@PathVariable("driveId") driveId: String?, key: String?): ResultBean {
        driveConfigService!!.refreshCache(driveId, key!!)
        return ResultBean.success()
    }

    @PostMapping("/{driveId}/auto-refresh/start")
    fun enableAutoRefresh(@PathVariable("driveId") driveId: String?): ResultBean {
        driveConfigService!!.startAutoCacheRefresh(driveId!!)
        return ResultBean.success()
    }

    @PostMapping("/{driveId}/auto-refresh/stop")
    fun disableAutoRefresh(@PathVariable("driveId") driveId: String?): ResultBean {
        driveConfigService!!.stopAutoCacheRefresh(driveId!!)
        return ResultBean.success()
    }

    @PostMapping("/{driveId}/clear")
    fun clearCache(@PathVariable("driveId") driveId: String?): ResultBean {
        driveConfigService!!.clearCache(driveId)
        return ResultBean.success()
    }
}