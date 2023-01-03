package org.sang.bean

/**
 * Created by sang on 2017/12/17.
 */
class RespBean {
    var status: String? = null
    var msg: String? = null

    constructor() {}
    constructor(status: String?, msg: String?) {
        this.status = status
        this.msg = msg
    }
}