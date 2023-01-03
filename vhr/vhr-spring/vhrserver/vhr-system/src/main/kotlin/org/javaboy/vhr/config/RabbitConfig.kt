package org.javaboy.vhr.config

org.springframework.amqp.core.Bindingimport org.springframework.context.annotation .Beanimport org.springframework.context.annotation .Configuration
@Configuration
class RabbitConfig {
    @Autowired
    var cachingConnectionFactory: CachingConnectionFactory? = null

    @Autowired
    var mailSendLogService: MailSendLogService? = null
    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(cachingConnectionFactory)
        rabbitTemplate.setConfirmCallback { data, ack, cause ->
            val msgId: String = data.getId()
            if (ack) {
                logger.info("$msgId:消息发送成功")
                mailSendLogService.updateMailSendLogStatus(msgId, 1) //修改数据库中的记录，消息投递成功
            } else {
                logger.info("$msgId:消息发送失败")
            }
        }
        rabbitTemplate.setReturnCallback { msg, repCode, repText, exchange, routingkey -> logger.info("消息发送失败") }
        return rabbitTemplate
    }

    @Bean
    fun mailQueue(): Queue {
        return Queue(MailConstants.MAIL_QUEUE_NAME, true)
    }

    @Bean
    fun mailExchange(): DirectExchange {
        return DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false)
    }

    @Bean
    fun mailBinding(): Binding {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME)
    }

    companion object {
        val logger = LoggerFactory.getLogger(RabbitConfig::class.java)
    }
}