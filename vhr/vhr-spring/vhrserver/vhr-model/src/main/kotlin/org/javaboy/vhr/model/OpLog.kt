package org.javaboy.vhr.model

import java.util.*

class OpLog {
    var id: Int? = null
    var adddate: Date? = null
    var operate: String? = null
        set(operate) {
            field = operate?.trim { it <= ' ' }
        }
    var hrid: Int? = null
}