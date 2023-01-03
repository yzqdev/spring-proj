package org.javaboy.vhr.model

object MailConstants {
    const val DELIVERING = 0 //消息投递中
    const val SUCCESS = 1 //消息投递成功
    const val FAILURE = 2 //消息投递失败
    const val MAX_TRY_COUNT = 3 //最大重试次数
    const val MSG_TIMEOUT = 1 //消息超时时间
    const val MAIL_QUEUE_NAME = "javaboy.mail.queue"
    const val MAIL_EXCHANGE_NAME = "javaboy.mail.exchange"
    const val MAIL_ROUTING_KEY_NAME = "javaboy.mail.routing.key"
}