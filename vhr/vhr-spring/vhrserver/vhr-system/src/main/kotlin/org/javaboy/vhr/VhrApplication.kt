package org.javaboy.vhr

import org.mybatis.spring.annotation.MapperScan

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "org.javaboy.vhr.mapper")
@EnableScheduling
@EnableWebSecurity
object VhrApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(VhrApplication::class.java, *args)
    }
}