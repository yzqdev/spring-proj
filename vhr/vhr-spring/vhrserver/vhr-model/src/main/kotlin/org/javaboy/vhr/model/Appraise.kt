package org.javaboy.vhr.model

import java.util.*

class Appraise {
    var id: Int? = null
    var eid: Int? = null
    var appdate: Date? = null
    var appresult: String? = null
        set(appresult) {
            field = appresult?.trim { it <= ' ' }
        }
    var appcontent: String? = null
        set(appcontent) {
            field = appcontent?.trim { it <= ' ' }
        }
    var remark: String? = null
        set(remark) {
            field = remark?.trim { it <= ' ' }
        }
}