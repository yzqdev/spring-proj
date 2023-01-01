package im.zhaojun.zfile.service

import cn.hutool.core.util.StrUtil
import im.zhaojun.zfile.model.entity.*
import im.zhaojun.zfile.repository.FilterConfigRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.FileSystems
import java.nio.file.Paths
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Slf4j
@Service
class FilterConfigService(  private val filterConfigRepository: FilterConfigRepository) {
 var log=LoggerFactory.getLogger(this.javaClass)
    fun findByDriveId(driveId: String): List<FilterConfig> {
        return filterConfigRepository!!.findByDriveId(driveId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun batchSave(filterConfigList: List<FilterConfig?>, driveId: String?) {
        filterConfigRepository!!.deleteByDriveId(driveId)
        filterConfigRepository.saveAll(filterConfigList)
    }

    /**
     * 指定驱动器下的文件名称, 根据过滤表达式判断是否会显示, 如果符合任意一条表达式, 则不显示, 反之则显示.
     * @param   driveId
     * 驱动器 ID
     * @param   fileName
     * 文件名
     * @return  是否显示
     */
    fun filterResultIsHidden(driveId: String, fileName: String?): Boolean {
        val filterConfigList = findByDriveId(driveId)
        for (filterConfig in filterConfigList) {
            val expression = filterConfig.expression
            if (StrUtil.isEmpty(expression)) {
                return false
            }
            try {
                val pathMatcher = FileSystems.getDefault().getPathMatcher("glob:$expression")
                val match = pathMatcher.matches(Paths.get(fileName))
                if (match) {
                    return true
                }
              log.debug("regex: {}, name {}, contains: {}", expression, fileName, match)
            } catch (e: Exception) {
               log.debug("regex: {}, name {}, parse error, skip expression", expression, fileName)
            }
        }
        return false
    }
}