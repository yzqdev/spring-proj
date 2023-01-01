package im.zhaojun.zfile.aspect

import im.zhaojun.zfile.cache.ZFileCache
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.service.DriveConfigService
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.Resource

/**
 * @author zhaojun
 * 缓存切面类, 用于访问文件夹时, 缓存文件列表内容.
 */
@Aspect
@Component
class FileListCacheAspect {
    @Resource
    private val zFileCache: ZFileCache? = null

    @Resource
    private val driveConfigService: DriveConfigService? = null

    /**
     * 缓存切面, 如果此驱动器开启了缓存, 则从缓存中取数据, 没有开启, 则直接调用方法.
     */
    @Around(value = "execution(public * im.zhaojun.zfile.service.base.AbstractBaseFileService.fileList(..))")
    @Throws(
        Throwable::class
    )
    fun around(point: ProceedingJoinPoint): Any {
        val result: List<FileItemDTO>

        // 获取请求路径
        val args = point.args
        val path = args[0].toString()

        // 获取当前驱动器
        val fileService = point.target as AbstractBaseFileService
        val driveId = fileService.driveId

        // 判断驱动器是否开启了缓存
        val driveConfig = driveConfigService!!.findById(driveId!!)
        val enableCache: Boolean = driveConfig.enableCache == true
        if (enableCache) {
            val cacheFileList = zFileCache!![driveId, path]
            if (cacheFileList == null) {
                result = Collections.unmodifiableList(point.proceed() as List<FileItemDTO>)
                zFileCache.put(driveId, path, result)
            } else {
                result = cacheFileList
            }
        } else {
            result = point.proceed() as List<FileItemDTO>
        }
        return result
    }
}