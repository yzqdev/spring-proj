package cn.hellohao

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling //@ServletComponentScan
//@EnableTransactionManagement(proxyTargetClass = true)

class TbedApplication
    fun main(args: Array<String>) {
       runApplication<TbedApplication>(*args)
    }
