package org.javaboy.vhr.model

import java.util.*

class AdjustSalary {
    var id: Int? = null
    var eid: Int? = null
    var asdate: Date? = null
    var beforesalary: Int? = null
    var aftersalary: Int? = null
    var reason: String? = null
        set(reason) {
            field = reason?.trim { it <= ' ' }
        }
    var remark: String? = null
        set(remark) {
            field = remark?.trim { it <= ' ' }
        }
}