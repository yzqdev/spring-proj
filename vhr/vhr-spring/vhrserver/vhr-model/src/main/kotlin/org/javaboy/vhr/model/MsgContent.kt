package org.javaboy.vhr.model

import java.util.*

class MsgContent {
    var id: Int? = null
    var title: String? = null
        set(title) {
            field = title?.trim { it <= ' ' }
        }
    var message: String? = null
        set(message) {
            field = message?.trim { it <= ' ' }
        }
    var createdate: Date? = null
}