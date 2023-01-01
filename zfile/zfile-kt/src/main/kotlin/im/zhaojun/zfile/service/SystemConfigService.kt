package im.zhaojun.zfile.service

import cn.hutool.core.convert.Convert
import cn.hutool.core.lang.Console
import cn.hutool.crypto.SecureUtil
import im.zhaojun.zfile.cache.ZFileCache
import im.zhaojun.zfile.exception.InvalidDriveException
import im.zhaojun.zfile.model.constant.SystemConfigConstant
import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.model.dto.SystemFrontConfigDTO
import im.zhaojun.zfile.model.entity.SystemConfig
import im.zhaojun.zfile.repository.SystemConfigRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service

/**
 * @author zhaojun
 */
@Slf4j
@Service
class SystemConfigService(private val zFileCache: ZFileCache, private val systemConfigRepository: SystemConfigRepository?, private val driveConfigService: DriveConfigService) {

    private val systemConfigClazz = SystemConfigDTO::class.java
var log=LoggerFactory.getLogger(this.javaClass)
    /**
     * 获取系统设置, 如果缓存中有, 则去缓存取, 没有则查询数据库并写入到缓存中.
     *
     * @return  系统设置
     */
    fun systemConfig(): SystemConfigDTO
      {
            val cacheConfig = zFileCache!!.config
            if (cacheConfig != null) {
                return cacheConfig
            }
            val systemConfigDTO = SystemConfigDTO()
            val systemConfigList = systemConfigRepository!!.findAll()
            println(systemConfigList)
            for (systemConfig in systemConfigList) {
                val key = systemConfig.key
                try {
                    val field = systemConfigClazz.getDeclaredField(key)
                    field.isAccessible = true
                    val strVal = systemConfig.value
                    val convertVal = Convert.convert(field.type, strVal)
                    field[systemConfigDTO] = convertVal
                } catch (e: NoSuchFieldException) {
                    log.error("通过反射, 将字段 {} 注入 SystemConfigDTO 时出现异常:", key, e)
                } catch (e: IllegalAccessException) {
                   log.error("通过反射, 将字段 {} 注入 SystemConfigDTO 时出现异常:", key, e)
                }
            }
            zFileCache.updateConfig(systemConfigDTO)
            return systemConfigDTO
        }

    /**
     * 更新系统设置, 并清空缓存中的内容.
     *
     * @param   systemConfigDTO
     * 系统
     */
    fun updateSystemConfig(systemConfigDTO: SystemConfigDTO?): SystemConfigDTO {
        val systemConfigList: MutableList<SystemConfig> = ArrayList()
        val fields = systemConfigClazz.declaredFields
        for (field in fields) {
            val key = field.name
            Console.log("key: {}", key)
            val systemConfig = systemConfigRepository!!.findByKey(key)
            if (systemConfig != null) {
                field.isAccessible = true
                var `val`: Any? = null
                try {
                    `val` = field[systemConfigDTO]
                    Console.log("这是系统配置: {}", systemConfig)
                    Console.log("这是post系统配置: {}", systemConfigDTO)
                    Console.log(`val`)
                } catch (e: IllegalAccessException) {
                 log.error("通过反射, 从 SystemConfigDTO 获取字段 {}  时出现异常:", key, e)
                }
                if (`val` != null) {
                    systemConfig.value = `val`.toString()
                    systemConfigList.add(systemConfig)
                }
            }
        }
        zFileCache!!.removeConfig()
        systemConfigRepository!!.saveAll(systemConfigList)
        return systemConfig
    }

    /**
     * 根据驱动器 ID, 获取对于前台页面的系统设置.
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  前台系统设置
     */
    fun getSystemFrontConfig(driveId: String): SystemFrontConfigDTO {
        val systemConfig = systemConfig
        val systemFrontConfigDTO = SystemFrontConfigDTO()
        BeanUtils.copyProperties(systemConfig, systemFrontConfigDTO)
        val driveConfig = driveConfigService!!.findById(driveId)
            ?: throw InvalidDriveException("此驱动器不存在或初始化失败, 请检查后台参数配置")
        systemFrontConfigDTO.searchEnable = driveConfig.searchEnable
        return systemFrontConfigDTO
    }

    /**
     * 更新后台账号密码
     *
     * @param   username
     * 用户名
     *
     * @param   password
     * 密码
     */
    fun updateUsernameAndPwd(username: String?, password: String?) {
        val usernameConfig = systemConfigRepository!!.findByKey(SystemConfigConstant.USERNAME)
        usernameConfig.value = username
        systemConfigRepository.save(usernameConfig)
        val encryptionPassword = SecureUtil.md5(password)
        val systemConfig = systemConfigRepository.findByKey(SystemConfigConstant.PASSWORD)
        systemConfig.value = encryptionPassword
        zFileCache!!.removeConfig()
        systemConfigRepository.save(systemConfig)
    }

    /**
     * 获取管理员名称
     *
     * @return  管理员名称
     */
    val adminUsername: String
        get() {
            val systemConfigDTO = systemConfig
            return systemConfigDTO.username
        }

    /**
     * 获取站点域名
     *
     * @return  站点域名
     */
    val domain: String
        get() {
            val systemConfigDTO = systemConfig
            return systemConfigDTO.domain
        }

    /**
     * 获取是否已安装初始化
     *
     * @return  是否已安装初始化
     */
    val isInstall: Boolean
        get() {
            val systemConfigDTO = systemConfig
            return StrUtil.isNotEmpty(systemConfigDTO.username)
        }
}