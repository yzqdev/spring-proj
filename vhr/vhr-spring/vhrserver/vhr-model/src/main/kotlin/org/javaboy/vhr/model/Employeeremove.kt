package org.javaboy.vhr.model

import java.util.*

class Employeeremove {
    var id: Int? = null
    var eid: Int? = null
    var afterdepid: Int? = null
    var afterjobid: Int? = null
    var removedate: Date? = null
    var reason: String? = null
        set(reason) {
            field = reason?.trim { it <= ' ' }
        }
    var remark: String? = null
        set(remark) {
            field = remark?.trim { it <= ' ' }
        }
}