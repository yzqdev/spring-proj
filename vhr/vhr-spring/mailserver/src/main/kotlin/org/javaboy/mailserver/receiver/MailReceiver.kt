package org.javaboy.mailserver.receiver

import com.rabbitmq.client.Channel
import org.javaboy.vhr.model.Employee
import org.javaboy.vhr.model.MailConstants
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.IOException
import java.util.*
import javax.mail.MessagingException

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-11-24 7:59
 */
@Component
class MailReceiver {
    @Autowired
    var javaMailSender: JavaMailSender? = null

    @Autowired
    var mailProperties: MailProperties? = null

    @Autowired
    var templateEngine: TemplateEngine? = null

    @Autowired
    var redisTemplate: StringRedisTemplate? = null
    @RabbitListener(queues = [MailConstants.MAIL_QUEUE_NAME])
    @Throws(IOException::class)
    fun handler(message: Message<*>, channel: Channel) {
        val employee = message.payload as Employee
        val headers = message.headers
        val tag = headers[AmqpHeaders.DELIVERY_TAG] as Long?
        val msgId = headers["spring_returned_message_correlation"] as String?
        if (redisTemplate!!.opsForHash<Any?, Any>().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            logger.info("$msgId:消息已经被消费")
            channel.basicAck(tag!!, false) //确认消息已消费
            return
        }
        //收到消息，发送邮件
        val msg = javaMailSender!!.createMimeMessage()
        val helper = MimeMessageHelper(msg)
        try {
            helper.setTo(employee.email)
            helper.setFrom(mailProperties!!.username)
            helper.setSubject("入职欢迎")
            helper.setSentDate(Date())
            val context = Context()
            context.setVariable("name", employee.name)
            context.setVariable("posName", employee.position.name)
            context.setVariable("joblevelName", employee.jobLevel.name)
            context.setVariable("departmentName", employee.department.name)
            val mail = templateEngine!!.process("mail", context)
            helper.setText(mail, true)
            javaMailSender!!.send(msg)
            redisTemplate!!.opsForHash<Any?, Any>().put("mail_log", msgId!!, "javaboy")
            channel.basicAck(tag!!, false)
            logger.info("$msgId:邮件发送成功")
        } catch (e: MessagingException) {
            channel.basicNack(tag!!, false, true)
            e.printStackTrace()
            logger.error("邮件发送失败：" + e.message)
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(MailReceiver::class.java)
    }
}