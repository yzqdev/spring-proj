package im.zhaojun.zfile.cache

import cn.hutool.core.util.StrUtil
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.repository.DriverConfigRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.annotation.Resource

/**
 * ZFile 缓存类
 *
 * @author zhaojun
 */
@Component
@Slf4j
class ZFileCache {
    @Resource
    private val driverConfigRepository: DriverConfigRepository? = null

    /**
     * 缓存过期时间
     */
    @Value("\${zfile.cache.timeout}")
    private val timeout: Long = 0

    /**
     * 缓存自动刷新间隔
     */
    @Value("\${zfile.cache.auto-refresh.interval}")
    private val autoRefreshInterval: Long = 0

    /**
     * 文件/文件对象缓存.
     *
     * ConcurrentMap<Integer></Integer>, ConcurrentHashMap<String></String>, List<FileItemDTO>>>
     * ConcurrentMap<driveId></driveId>, ConcurrentHashMap<key></key>, value>>
     *
     * driveId: 驱动器 ID
     * key: 文件夹路径
     * value: 文件夹中内容
    </FileItemDTO> */
    private val drivesCache: ConcurrentMap<String, MyTimedCache<DriveCacheKey?, List<FileItemDTO>>> =
        ConcurrentHashMap()
    /**
     * 从缓存中获取系统设置
     *
     * @return  系统设置
     */
    /**
     * 系统设置缓存
     */
    var config: SystemConfigDTO? = null
        private set

    /**
     * 写入缓存
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   key
     * 文件夹路径
     *
     * @param   value
     * 文件夹中列表
     */
    @Synchronized
    fun put(driveId: String, key: String?, value: List<FileItemDTO>) {
        getCacheByDriveId(driveId).put(DriveCacheKey(driveId, key), value)
    }

    /**
     * 获取指定驱动器, 某个文件夹的名称
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   key
     * 文件夹路径
     *
     * @return  驱动器中文件夹的内容
     */
    operator fun get(driveId: String, key: String?): List<FileItemDTO> {
        return getCacheByDriveId(driveId)[DriveCacheKey(driveId, key), false]
    }

    /**
     * 清空指定驱动器的缓存.
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun clear(driveId: String) {
        if (ZFileCache.log.isDebugEnabled()) {
            ZFileCache.log.debug("清空驱动器所有缓存, driveId: {}", driveId)
        }
        getCacheByDriveId(driveId).clear()
    }

    /**
     * 获取指定驱动器中已缓存文件夹数量
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  已缓存文件夹数量
     */
    fun cacheCount(driveId: String): Int {
        return getCacheByDriveId(driveId).size()
    }

    /**
     * 指定驱动器, 根据文件及文件名查找相关的文件
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   key
     * 搜索键, 可匹配文件夹名称和文件名称.
     *
     * @return  搜索结果, 包含文件夹和文件.
     */
    fun find(driveId: String, key: String?): List<FileItemDTO> {
        val result: MutableList<FileItemDTO> = ArrayList()
        val driveConfig = driverConfigRepository!!.getOne(driveId)
        val searchContainEncryptedFile: Boolean = driveConfig.getSearchContainEncryptedFile()
        val ignoreCase: Boolean = driveConfig.getSearchIgnoreCase()
        for (fileItemList in getCacheByDriveId(driveId)) {
            // 过滤加密文件
            if (!searchContainEncryptedFile && isEncryptedFolder(fileItemList)) {
                continue
            }
            for (fileItemDTO in fileItemList) {
                var testResult: Boolean

                // 根据是否需要忽略大小写来匹配文件(夹)名
                testResult = if (ignoreCase) {
                    StrUtil.containsIgnoreCase(fileItemDTO.getName(), key)
                } else {
                    fileItemDTO.getName().contains(key)
                }
                if (testResult) {
                    result.add(fileItemDTO)
                }
            }
        }
        return result
    }

    /**
     * 获取所有缓存 key (文件夹名称)
     *
     * @return      所有缓存 key
     */
    fun keySet(driveId: String): Set<String> {
        val cacheObjIterator = getCacheByDriveId(driveId).cacheObjIterator()
        val keys: MutableSet<String> = HashSet()
        while (cacheObjIterator.hasNext()) {
            keys.add(cacheObjIterator.next().key.getKey())
        }
        return keys
    }

    /**
     * 从缓存中删除指定驱动器的某个路径的缓存
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   key
     * 文件夹路径
     */
    fun remove(driveId: String, key: String?) {
        getCacheByDriveId(driveId).remove(DriveCacheKey(driveId, key))
    }

    /**
     * 更新缓存中的系统设置
     *
     * @param   systemConfigCache
     * 系统设置
     */
    fun updateConfig(systemConfigCache: SystemConfigDTO?) {
        config = systemConfigCache
    }

    /**
     * 清空系统设置缓存
     */
    fun removeConfig() {
        config = null
    }

    /**
     * 判断此文件夹是否为加密文件夹 (包含)
     *
     * @param   list
     * 文件夹中的内容
     *
     * @return  返回此文件夹是否是加密的 ().
     */
    private fun isEncryptedFolder(list: List<FileItemDTO>): Boolean {
        // 遍历文件判断是否包含
        for (fileItemDTO in list) {
            if (ZFileConstant.PASSWORD_FILE_NAME == fileItemDTO.getName()) {
                return true
            }
        }
        return false
    }

    /**
     * 获取指定驱动器对应的缓存
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  驱动器对应的缓存
     */
    @Synchronized
    private fun getCacheByDriveId(driveId: String): MyTimedCache<DriveCacheKey?, List<FileItemDTO>> {
        var driveCache = drivesCache[driveId]
        if (driveCache == null) {
            driveCache = MyTimedCache(timeout * 1000)
            drivesCache[driveId] = driveCache
            startAutoCacheRefresh(driveId)
        }
        return driveCache
    }

    /**
     * 获取指定驱动器的缓存命中数
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  缓存命中数
     */
    fun getHitCount(driveId: String): Int {
        return Math.toIntExact(getCacheByDriveId(driveId).hitCount)
    }

    /**
     * 获取指定驱动器的缓存未命中数
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @return  缓存未命中数
     */
    fun getMissCount(driveId: String): Int {
        return Math.toIntExact(getCacheByDriveId(driveId).missCount)
    }

    /**
     * 开启缓存自动刷新
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun startAutoCacheRefresh(driveId: String) {
        if (ZFileCache.log.isDebugEnabled()) {
            ZFileCache.log.debug("开启缓存自动刷新 driveId: {}", driveId)
        }
        val driveConfig = driverConfigRepository!!.findById(driveId).get()
        val autoRefreshCache: Boolean = driveConfig.getAutoRefreshCache()
        if (autoRefreshCache != null && autoRefreshCache) {
            var driveCache = drivesCache[driveId]
            if (driveCache == null) {
                driveCache = MyTimedCache(timeout * 1000)
                drivesCache[driveId] = driveCache
            }
            driveCache.schedulePrune(autoRefreshInterval * 1000)
        }
    }

    /**
     * 停止缓存自动刷新
     *
     * @param   driveId
     * 驱动器 ID
     */
    fun stopAutoCacheRefresh(driveId: String) {
        if (ZFileCache.log.isDebugEnabled()) {
            ZFileCache.log.debug("停止缓存自动刷新 driveId: {}", driveId)
        }
        val driveCache = drivesCache[driveId]
        driveCache?.cancelPruneSchedule()
    }
}