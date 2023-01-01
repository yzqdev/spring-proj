package im.zhaojun.zfile.context

import com.alibaba.fastjson.JSON
import im.zhaojun.zfile.exception.InvalidDriveException
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.repository.DriverConfigRepository
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import im.zhaojun.zfile.util.SpringContextHolder.Companion.getBean
import im.zhaojun.zfile.util.SpringContextHolder.Companion.getBeansOfType
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.Resource

/**
 * 每个驱动器对应一个 Service, 其中初始化好了与对象存储的连接信息.
 * 此驱动器上下文环境用户缓存每个 Service, 避免重复创建连接.
 * @author zhaojun
 */
@Component
@DependsOn("springContextHolder")
@Slf4j
class DriveContext : ApplicationContextAware {
    @Resource
    private val driverConfigRepository: DriverConfigRepository? = null
var log =LoggerFactory.getLogger(this.javaClass)
    /**
     * 项目启动时, 自动调用数据库已存储的所有驱动器进行初始化.
     */
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        val list = driverConfigRepository!!.findAll()
        for (driveConfig in list) {
            try {
                init(driveConfig.id)
               log.info("启动时初始化驱动器成功, 驱动器信息: {}", JSON.toJSONString(driveConfig))
            } catch (e: Exception) {
           log.error("启动时初始化驱动器失败, 驱动器信息: {}", JSON.toJSONString(driveConfig), e)
            }
        }
    }

    /**
     * 初始化指定驱动器的 Service, 添加到上下文环境中.
     *
     * @param   driveId
     * 驱动器 ID.
     */
    fun init(driveId: String) {
        val baseFileService = getBeanByDriveId(driveId)
        if (baseFileService != null) {
            if (log.isDebugEnabled()) {
                log.debug("尝试初始化驱动器, driveId: {}", driveId)
            }
            baseFileService.init(driveId)
            if (log.isDebugEnabled()) {
                log.debug("初始化驱动器成功, driveId: {}", driveId)
            }
            drivesServiceMap[driveId] = baseFileService
        }
    }

    /**
     * 获取指定驱动器的 Service.
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  驱动器对应的 Service
     */
    operator fun get(driveId: String): AbstractBaseFileService {
        return drivesServiceMap[driveId]
            ?: throw InvalidDriveException("此驱动器不存在或初始化失败, 请检查后台参数配置")
    }

    /**
     * 销毁指定驱动器的 Service.
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun destroy(driveId: String) {
        if (log.isDebugEnabled()) {
            log.debug("清理驱动器上下文对象, driveId: {}", driveId)
        }
        drivesServiceMap.remove(driveId)
    }

    /**
     * 获取指定驱动器对应的 Service, 状态为未初始化
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  驱动器对应未初始化的 Service
     */
    private fun getBeanByDriveId(driveId: String): AbstractBaseFileService? {
        log.info(driveId + "驱动器")
        val driveConfig = driverConfigRepository!!.findById(driveId)
        if (driveConfig.isPresent) {
            val storageTypeEnum: StorageTypeEnum = driveConfig.get().type
            val beansOfType = getBeansOfType(
                AbstractBaseFileService::class.java
            )
            for (value in beansOfType.values) {
                if (value.storageTypeEnum == storageTypeEnum) {
                    return getBean(value.javaClass)
                }
            }
        }
        return null
    }

    /**
     * 更新上下文环境中的驱动器 ID
     *
     * @param   updateId
     * 驱动器原 ID
     *
     * @param   newId
     * 驱动器新 ID
     */
    fun updateDriveId(updateId: String, newId: String) {
        val fileService = drivesServiceMap.remove(updateId)
        if (fileService != null) {
            fileService.driveId=newId
        }
        drivesServiceMap[newId] = fileService
    }

    companion object {
        /**
         * Map<Integer></Integer>, AbstractBaseFileService>
         * Map<驱动器 ID, 驱动器连接 Service>
        </驱动器> */
        private val drivesServiceMap: MutableMap<String, AbstractBaseFileService?> = ConcurrentHashMap()
    }
}