package im.zhaojun.zfile.cache

import cn.hutool.cache.impl.TimedCache
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.util.SpringContextHolder.Companion.getBean
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory

/**
 * @author zhaojun
 */
@Slf4j
class MyTimedCache<K, V>(timeout: Long) : TimedCache<K, V>(timeout) {
    private var driveContext: DriveContext? = null
var log=LoggerFactory.getLogger(this.javaClass)
    //public MyTimedCache(long timeout, Map<K, CacheObj<K, V>> map) {
    //    super(timeout, map);
    //}
    override fun onRemove(key: K, cachedObject: V) {
        if (driveContext == null) {
            driveContext = getBean(DriveContext::class.java)
        }
        val cacheKey = key as DriveCacheKey
        val baseFileService = driveContext!![cacheKey.driveId]
        if ( log.isDebugEnabled()) {
             log.debug("尝试刷新缓存: {}", cacheKey)
        }
        if (baseFileService == null) {
           log.error("尝试刷新缓存: {}, 时出现异常, 驱动器已不存在", cacheKey)
            return
        }
        try {
            baseFileService.fileList(cacheKey.key)
        } catch (e: Exception) {
             log.error("尝试刷新缓存 {} 失败", cacheKey)
            e.printStackTrace()
        }
    }
}