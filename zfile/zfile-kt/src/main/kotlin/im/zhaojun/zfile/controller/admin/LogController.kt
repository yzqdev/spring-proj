package im.zhaojun.zfile.controller.admin

import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.ZipUtil
import im.zhaojun.zfile.util.FileUtil.exportSingleThread
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * 日志相关 Controller
 * @author zhaojun
 */
@RestController
@RequestMapping("/admin")
@Slf4j
class LogController {
    var log=LoggerFactory.getLogger(this.javaClass)
    /**
     * 系统日志下载
     */
    @GetMapping("/log")
    fun downloadLog(): ResponseEntity<Any> {
        if ( log.isDebugEnabled()) {
            log.debug("下载诊断日志")
        }
        val userHome = System.getProperty("user.home")
        val fileZip = ZipUtil.zip("$userHome/.zfile/logs")
        val currentDate = DateUtil.format(Date(), "yyyy-MM-dd HH:mm:ss")
        return exportSingleThread(fileZip, "ZFile 诊断日志 - $currentDate.zip")
    }
}