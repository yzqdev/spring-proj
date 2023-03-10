package im.zhaojun.zfile.schedule

import com.alibaba.fastjson.JSON
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.model.entity.DriveConfig
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.DriveConfigService
import im.zhaojun.zfile.service.baseimport.AbstractOneDriveServiceBase
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.function.Consumer
import javax.annotation.Resource

/**
 * 计划任务工具类
 * @author zhaojun
 */
@Configuration
@EnableScheduling
@Slf4j
class OneDriveTokenRefreshSchedule {
    @Resource
    private val driveConfigService: DriveConfigService? = null

    @Resource
    private val driveContext: DriveContext? = null

    /**
     * 项目启动 30 秒后, 每 15 分钟执行一次刷新 OneDrive Token 的定时任务.
     */
    @Scheduled(fixedRate = 1000 * 60 * 10, initialDelay = 1000 * 30)
    fun autoRefreshOneDriveToken() {
        try {
            OneDriveTokenRefreshSchedule.log.debug("尝试调用 OneDrive 自动刷新 AccessToken 定时任务")
            val driveConfigList: MutableList<DriveConfig> = driveConfigService.findByType(StorageTypeEnum.ONE_DRIVE)
            driveConfigList.addAll(driveConfigService.findByType(StorageTypeEnum.ONE_DRIVE_CHINA))
            driveConfigList.forEach(Consumer<DriveConfig> { driveConfig: DriveConfig ->
                try {
                    val driveService: AbstractOneDriveServiceBase =
                        driveContext.get(driveConfig.getId()) as AbstractOneDriveServiceBase
                    driveService.refreshOneDriveToken()
                    OneDriveTokenRefreshSchedule.log.info(
                        "尝试刷新 OneDrive Token, DriveInfo: {}",
                        JSON.toJSONString(driveConfig)
                    )
                } catch (e: Exception) {
                    OneDriveTokenRefreshSchedule.log.error(
                        "刷新 OneDrive Token 失败, DriveInfo: {}",
                        JSON.toJSONString(driveConfig),
                        e
                    )
                }
            })
        } catch (e: Throwable) {
            OneDriveTokenRefreshSchedule.log.error("尝试调用 OneDrive 自动刷新 AccessToken 定时任务出现未知异常", e)
        }
    }
}