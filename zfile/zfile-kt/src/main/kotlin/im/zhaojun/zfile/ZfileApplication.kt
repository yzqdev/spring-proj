package im.zhaojun.zfile

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

/**
 * @author zhaojun
 */
@EnableAsync
@SpringBootApplication
@EnableJpaRepositories("im.zhaojun.zfile.repository")
@EnableAspectJAutoProxy(exposeProxy = true)
class ZfileApplication

    fun main(args: Array<String>) {
       runApplication<ZfileApplication>(*args)
    }
