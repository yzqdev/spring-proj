package org.javaboy.mailserver

import org.javaboy.vhr.model.MailConstants
import org.springframework.amqp.core.Queue
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class MailserverApplication {
    @Bean
    fun queue(): Queue {
        return Queue(MailConstants.MAIL_QUEUE_NAME)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(MailserverApplication::class.java, *args)
        }
    }
}