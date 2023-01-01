package cn.hellohao.service.impl

import cn.hellohao.auth.filter.SubjectFilter
import cn.hellohao.mapper.KeysMapper
import cn.hellohao.model.entity.StorageKey
import cn.hellohao.util.Print
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/28 15:33
 */
@Component
@Order(2)
class InitializationStorage( private val keysMapper: KeysMapper) : CommandLineRunner {
    @Value("\${CROS_ALLOWED_ORIGINS}")
    private lateinit var allowedOrigins: Array<String>

    @Value("\${server.port}")
    private val port: String? = null

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        SubjectFilter.WEBHOST = allowedOrigins
        val name = ManagementFactory.getRuntimeMXBean().name
        val pid = name.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        intiStorage()
        sout()
    }

    fun intiStorage() {
        val keylist: List<StorageKey> = keysMapper.keys
        for (key in keylist) {
            if (key.storageType != 0 && key.storageType != null) {
                var ret = 0
                if (key.storageType == 1) {
                    ret = NOSImageupload.Companion.Initialize(key)
                } else if (key.storageType == 2) {
                    ret = OSSImageupload.Companion.Initialize(key)
                } else if (key.storageType == 3) {
                    ret = USSImageupload.Companion.Initialize(key)
                } else if (key.storageType == 4) {
                    ret = KODOImageupload.Companion.Initialize(key)
                } else if (key.storageType == 6) {
                    ret = COSImageupload.Companion.Initialize(key)
                } else if (key.storageType == 7) {
                    ret = FTPImageupload.Companion.Initialize(key)
                } else if (key.storageType == 8) {
                    ret = UFileImageupload.Companion.Initialize(key)
                }
            }
        }
    }

    fun sout() {
        Print.Normal("______________________________________________")
        Print.Normal("              Hellohao Tbed                ")
        Print.Normal("     Successful startup of the program      ")
        Print.Normal("     is OK!  Open http://localhost:$port       ")
        Print.Normal("     is OK!  Open swagger http://localhost:$port/swagger-ui.html")
        Print.Normal("______________________________________________")
    }
}