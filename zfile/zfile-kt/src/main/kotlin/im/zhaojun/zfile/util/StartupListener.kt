package im.zhaojun.zfile.util

import cn.hutool.core.net.NetUtil
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.Environment
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 项目启动监听器, 当项目启动时, 遍历当前对象存储的所有内容, 添加到缓存中.
 * @author zhaojun
 */
@Component
@Slf4j
class StartupListener( private val environment: Environment) : ApplicationListener<ApplicationStartedEvent?> {
  var log=LoggerFactory.getLogger(this.javaClass)
    override fun onApplicationEvent(@NonNull event: ApplicationStartedEvent?) {
        printStartInfo()
    }

    private fun printStartInfo() {
        val serverPort = environment!!.getProperty("server.port", "8080")
        val localIps = NetUtil.localIps()
        val indexAddr = StringBuilder()
        val indexAdminAddr = StringBuilder()
        for (localIp in localIps) {
            val addr = String.format("http://%s:%s", localIp, serverPort)
            indexAddr.append(addr).append("\t")
            indexAdminAddr.append(addr).append("/admin").append("\t")
        }
       log.info("ZFile started at          $indexAddr")
        log.info("ZFile Admin started at    $indexAdminAddr")
    } // private void cacheAllFile() {
    //     try {
    //         fileAsyncCacheService.cacheGlobalFile();
    //     } catch (Exception e) {
    //         throw new InitializeException("初始化缓存异常.", e);
    //     }
    // }
}