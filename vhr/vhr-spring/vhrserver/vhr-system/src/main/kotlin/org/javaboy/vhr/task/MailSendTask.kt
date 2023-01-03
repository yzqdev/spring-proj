package org.javaboy.vhr.task

import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer

@Component
class MailSendTask {
    @Autowired
    var mailSendLogService: MailSendLogService? = null

    @Autowired
    var rabbitTemplate: RabbitTemplate? = null

    @Autowired
    var employeeService: EmployeeService? = null
    @Scheduled(cron = "0/10 * * * * ?")
    fun mailResendTask() {
        val logs: List<MailSendLog> = mailSendLogService.getMailSendLogsByStatus()
        println("开始执行定时任务")
        if (logs == null || logs.size == 0) {
            return
        }
        logs.forEach(Consumer<MailSendLog> { mailSendLog: MailSendLog ->
            if (mailSendLog.getCount() >= 3) {
                mailSendLogService.updateMailSendLogStatus(mailSendLog.getMsgId(), 2) //直接设置该条消息发送失败
            } else {
                mailSendLogService.updateCount(mailSendLog.getMsgId(), Date())
                val emp: Employee = employeeService.getEmployeeById(mailSendLog.getEmpId())
                rabbitTemplate.convertAndSend(
                    MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    emp,
                    CorrelationData(mailSendLog.getMsgId())
                )
            }
        })
    }
}