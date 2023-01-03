package org.javaboy.vhr.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.util.*

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class MailSendLog {
    private val msgId: String? = null
    private val empId: Int? = null

    //0 消息投递中   1 投递成功   2投递失败
    private val status: Int? = null
    private val routeKey: String? = null
    private val exchange: String? = null
    private val count: Int? = null
    private val tryTime: Date? = null
    private val createTime: Date? = null
    private val updateTime: Date? = null
}