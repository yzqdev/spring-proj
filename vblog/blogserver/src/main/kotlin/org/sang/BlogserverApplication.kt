package org.sang

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling //开启定时任务支持

class BlogserverApplication

    fun main(args: Array<String>) {
     runApplication<BlogserverApplication>(*args)
    }
