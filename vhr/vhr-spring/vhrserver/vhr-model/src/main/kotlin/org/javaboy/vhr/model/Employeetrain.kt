package org.javaboy.vhr.model

import java.util.*

class Employeetrain {
    var id: Int? = null
    var eid: Int? = null
    var traindate: Date? = null
    var traincontent: String? = null
        set(traincontent) {
            field = traincontent?.trim { it <= ' ' }
        }
    var remark: String? = null
        set(remark) {
            field = remark?.trim { it <= ' ' }
        }
}