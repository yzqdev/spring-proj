package org.javaboy.vhr.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class WsController {
    @Autowired
    var simpMessagingTemplate: SimpMessagingTemplate? = null
    @MessageMapping("/ws/chat")
    fun handleMsg(authentication: Authentication, chatMsg: ChatMsg) {
        val hr: Hr = authentication.getPrincipal() as Hr
        chatMsg.from = hr.getUsername()
        chatMsg.fromNickname = hr.name
        chatMsg.date = Date()
        simpMessagingTemplate.convertAndSendToUser(chatMsg.to, "/queue/chat", chatMsg)
    }
}