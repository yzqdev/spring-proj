package org.javaboy.vhr.model

import java.util.*

class Employeeec {
    var id: Int? = null
    var eid: Int? = null
    var ecdate: Date? = null
    var ecreason: String? = null
        set(ecreason) {
            field = ecreason?.trim { it <= ' ' }
        }
    var ecpoint: Int? = null
    var ectype: Int? = null
    var remark: String? = null
        set(remark) {
            field = remark?.trim { it <= ' ' }
        }
}