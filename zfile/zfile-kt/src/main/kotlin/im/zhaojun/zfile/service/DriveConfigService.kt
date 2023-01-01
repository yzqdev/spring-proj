package im.zhaojun.zfile.service

import com.alibaba.fastjson.JSONObject
import im.zhaojun.zfile.cache.ZFileCache
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.context.StorageTypeContext
import im.zhaojun.zfile.exception.InitializeDriveException
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.CacheInfoDTO
import im.zhaojun.zfile.model.dto.DriveConfigDTO
import im.zhaojun.zfile.model.dto.StorageStrategyConfig
import im.zhaojun.zfile.model.entity.DriveConfig
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.repository.DriverConfigRepository
import im.zhaojun.zfile.repository.FilterConfigRepository
import im.zhaojun.zfile.repository.ShortLinkConfigRepository
import im.zhaojun.zfile.repository.StorageConfigRepository
import im.zhaojun.zfile.util.StringUtils
import im.zhaojun.zfile.util.StringUtils.concatUrl
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.reflect.Field
import javax.annotation.Resource

/**
 * 驱动器 Service 类
 * @author zhaojun
 */
@Slf4j
@Service
class DriveConfigService(
    private val driverConfigRepository: DriverConfigRepository,
    private val storageConfigRepository: StorageConfigRepository,
    private val filterConfigRepository: FilterConfigRepository,
    private val shortLinkConfigRepository: ShortLinkConfigRepository,
    private val driveContext: DriveContext,
    private val zFileCache: ZFileCache
) {


    /**
     * 获取所有驱动器列表
     *
     * @return  驱动器列表
     */
    fun list(): List<DriveConfig> {
        val sort = Sort.by(Sort.Direction.ASC, "orderNum")
        return driverConfigRepository!!.findAll(sort)
    }

    /**
     * 获取所有已启用的驱动器列表
     *
     * @return  已启用的驱动器列表
     */
    fun listOnlyEnable(): List<DriveConfig> {
        val driveConfig = DriveConfig()
        driveConfig.enable = true
        val example = Example.of(driveConfig)
        val sort = Sort.by(Sort.Direction.ASC, "orderNum")
        return driverConfigRepository!!.findAll(example, sort)
    }

    /**
     * 获取指定驱动器设置
     *
     * @param   id
     * 驱动器 ID
     *
     * @return  驱动器设置
     */
    fun findById(id: String): DriveConfig {
        return driverConfigRepository!!.findById(id).orElse(null)
    }

    /**
     * 获取指定驱动器 DTO 对象, 此对象包含详细的参数设置.
     *
     * @param   id
     * 驱动器 ID
     *
     * @return  驱动器 DTO
     */
    fun findDriveConfigDTOById(id: String): DriveConfigDTO {
        val driveConfig = driverConfigRepository!!.getOne(id)
        val driveConfigDTO = DriveConfigDTO()
        val storageConfigList = storageConfigRepository!!.findByDriveId(driveConfig.id)
        val defaultSwitchToImgMode = driveConfig.defaultSwitchToImgMode
        if (defaultSwitchToImgMode == null) {
            driveConfig.defaultSwitchToImgMode = false
        }
        BeanUtils.copyProperties(driveConfig, driveConfigDTO)
        val storageStrategyConfig = StorageStrategyConfig()
        for (storageConfig in storageConfigList) {
            val key = storageConfig.key
            val value = storageConfig.value
            var declaredField: Field
            try {
                declaredField = STORAGE_STRATEGY_CONFIG_CLASS.getDeclaredField(key)
                declaredField.isAccessible = true
                if (StorageConfigConstant.IS_PRIVATE == key) {
                    declaredField[storageStrategyConfig] = java.lang.Boolean.valueOf(value)
                } else {
                    declaredField[storageStrategyConfig] = value
                }
            } catch (e: NoSuchFieldException) {
                DriveConfigService.log.error("通过反射, 将字段 {} 注入 DriveConfigDTO 时出现异常:", key, e)
            } catch (e: IllegalAccessException) {
                DriveConfigService.log.error("通过反射, 将字段 {} 注入 DriveConfigDTO 时出现异常:", key, e)
            }
        }
        driveConfigDTO.storageStrategyConfig = storageStrategyConfig
        return driveConfigDTO
    }

    /**
     * 获取指定驱动器的存储策略.
     *
     * @param   id
     * 驱动器 ID
     *
     * @return  驱动器对应的存储策略.
     */
    fun findStorageTypeById(id: String): StorageTypeEnum {
        return driverConfigRepository!!.findById(id).get().type
    }

    /**
     * 更新驱动器设置
     * @param driveConfig   驱动器设置
     */
    fun updateDriveConfig(driveConfig: DriveConfig) {
        driverConfigRepository!!.save(driveConfig)
    }

    /**
     * 保存驱动器基本信息及其对应的参数设置
     *
     * @param driveConfigDTO    驱动器 DTO 对象
     */
    @Transactional(rollbackFor = [Exception::class])
    fun saveDriveConfigDTO(driveConfigDTO: DriveConfigDTO) {

        // 判断是新增还是修改
        val updateFlag = driveConfigDTO.id != null

        // 保存基本信息
        val driveConfig = DriveConfig()
        val storageType = driveConfigDTO.type
        BeanUtils.copyProperties(driveConfigDTO, driveConfig)
        if (driveConfig.id == null) {
            val nextId = selectNextId()
            driveConfig.id = nextId
        }
        DriveConfigService.log.info("这是driver")
        DriveConfigService.log.info(driveConfig.toString())
        driverConfigRepository!!.save(driveConfig)

        // 保存存储策略设置.
        val storageStrategyConfig = driveConfigDTO.storageStrategyConfig
        val storageTypeService = StorageTypeContext.getStorageTypeService(storageType)
        val storageConfigList = storageTypeService.storageStrategyConfigList()
        storageConfigRepository!!.deleteByDriveId(driveConfigDTO.id)
        for (storageConfig in storageConfigList!!) {
            val key = storageConfig!!.key
            try {
                val field = STORAGE_STRATEGY_CONFIG_CLASS.getDeclaredField(key)
                field.isAccessible = true
                val o = field[storageStrategyConfig]
                val value = o?.toString()
                storageConfig.value = value
                storageConfig.type = storageType
                storageConfig.driveId = driveConfig.id
            } catch (e: IllegalAccessException) {
                DriveConfigService.log.error("通过反射, 从 StorageStrategyConfig 中获取字段 {} 时出现异常:", key, e)
            } catch (e: NoSuchFieldException) {
                DriveConfigService.log.error("通过反射, 从 StorageStrategyConfig 中获取字段 {} 时出现异常:", key, e)
            }
        }
        storageConfigRepository.saveAll(storageConfigList)
        driveContext!!.init(driveConfig.id)
        val driveService = driveContext[driveConfig.id]
        if (driveService.isUnInitialized) {
            throw InitializeDriveException("初始化异常, 请检查配置是否正确.")
        }
        if (driveConfig.autoRefreshCache) {
            startAutoCacheRefresh(driveConfig.id)
        } else if (updateFlag) {
            stopAutoCacheRefresh(driveConfig.id)
        }
    }

    /**
     * 查询驱动器最大的 ID
     *
     * @return  驱动器最大 ID
     */
    fun selectNextId(): String {
        val maxId = driverConfigRepository!!.selectMaxId()
        return if (maxId == null) {
            "1"
        } else {
            maxId + 1
        }
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
    @Transactional
    fun updateId(updateId: String, newId: String) {
        zFileCache!!.clear(updateId)
        driverConfigRepository!!.updateId(updateId, newId)
        storageConfigRepository!!.updateDriveId(updateId, newId)
        filterConfigRepository!!.updateDriveId(updateId, newId)
        val updateSubPath = concatUrl(StringUtils.DELIMITER_STR, ZFileConstant.DIRECT_LINK_PREFIX, updateId)
        val newSubPath = concatUrl(StringUtils.DELIMITER_STR, ZFileConstant.DIRECT_LINK_PREFIX, newId)
        shortLinkConfigRepository!!.updateUrlDriveId(updateSubPath, newSubPath)
        driveContext!!.updateDriveId(updateId, newId)
    }

    /**
     * 删除指定驱动器设置, 会级联删除其参数设置
     *
     * @param   id
     * 驱动器 ID
     */
    @Transactional(rollbackFor = [Exception::class])
    fun deleteById(id: String) {
        if (DriveConfigService.log.isDebugEnabled()) {
            DriveConfigService.log.debug("尝试删除驱动器, driveId: {}", id)
        }
        val driveConfig = driverConfigRepository!!.getOne(id)
        driverConfigRepository.deleteById(id)
        storageConfigRepository!!.deleteByDriveId(id)
        if (driveConfig.enableCache) {
            zFileCache!!.stopAutoCacheRefresh(id)
            zFileCache.clear(id)
        }
        driveContext!!.destroy(id)
        if (DriveConfigService.log.isDebugEnabled()) {
            DriveConfigService.log.debug("尝试删除驱动器成功, 已清理相关数据, driveId: {}", id)
        }
    }

    /**
     * 根据存储策略类型获取所有驱动器
     *
     * @param   type
     * 存储类型
     *
     * @return  指定存储类型的驱动器
     */
    fun findByType(type: StorageTypeEnum?): List<DriveConfig> {
        return driverConfigRepository!!.findByType(type)
    }

    /**
     * 更新指定驱动器的缓存启用状态
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   cacheEnable
     * 是否启用缓存
     */
    fun updateCacheStatus(driveId: String, cacheEnable: Boolean?) {
        val driveConfig = findById(driveId)
        if (driveConfig != null) {
            driveConfig.enableCache = cacheEnable
            driverConfigRepository!!.save(driveConfig)
        }
    }

    /**
     * 获取指定驱动器的缓存信息
     * @param   driveId
     * 驱动器 ID
     * @return  缓存信息
     */
    fun findCacheInfo(driveId: String?): CacheInfoDTO {
        val hitCount = zFileCache!!.getHitCount(driveId)
        val missCount = zFileCache.getMissCount(driveId)
        val keys = zFileCache.keySet(driveId)
        val cacheCount = keys.size
        return CacheInfoDTO(cacheCount, hitCount, missCount, keys)
    }

    /**
     * 刷新指定 key 的缓存:
     * 1. 清空此 key 的缓存.
     * 2. 重新调用方法写入缓存.
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   key
     * 缓存 key (文件夹名称)
     */
    @Throws(Exception::class)
    fun refreshCache(driveId: String?, key: String) {
        if (DriveConfigService.log.isDebugEnabled()) {
            DriveConfigService.log.debug("手动刷新缓存 driveId: {}, key: {}", driveId, key)
        }
        zFileCache!!.remove(driveId, key)
        val baseFileService = driveContext!![driveId]
        baseFileService.fileList(key)
    }

    /**
     * 开启缓存自动刷新
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun startAutoCacheRefresh(driveId: String) {
        val driveConfig = findById(driveId)
        driveConfig.autoRefreshCache = true
        driverConfigRepository!!.save(driveConfig)
        zFileCache!!.startAutoCacheRefresh(driveId)
    }

    /**
     * 停止缓存自动刷新
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun stopAutoCacheRefresh(driveId: String) {
        val driveConfig = findById(driveId)
        driveConfig.autoRefreshCache = false
        driverConfigRepository!!.save(driveConfig)
        zFileCache!!.stopAutoCacheRefresh(driveId)
    }

    /**
     * 清理缓存
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun clearCache(driveId: String?) {
        zFileCache!!.clear(driveId)
    }

    /**
     * 交换驱动器排序
     */
    @Transactional(rollbackFor = [Exception::class])
    fun saveDriveDrag(driveConfigs: List<JSONObject>) {
        for (i in driveConfigs.indices) {
            val item = driveConfigs[i]
            driverConfigRepository!!.updateSetOrderNumById(i, item.getInteger("id"))
        }
    }

    companion object {
        val STORAGE_STRATEGY_CONFIG_CLASS = StorageStrategyConfig::class.java
    }
}