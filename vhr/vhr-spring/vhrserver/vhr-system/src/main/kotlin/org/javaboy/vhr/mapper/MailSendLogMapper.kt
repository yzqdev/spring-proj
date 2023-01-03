package org.javaboy.vhr.mapper

java.util.*
interface MailSendLogMapper {
    fun updateMailSendLogStatus(@Param("msgId") msgId: String?, @Param("status") status: Int?): Int?
    fun insert(mailSendLog: MailSendLog?): Int?
    val mailSendLogsByStatus: List<Any?>?
    fun updateCount(@Param("msgId") msgId: String?, @Param("date") date: Date?): Int?
}