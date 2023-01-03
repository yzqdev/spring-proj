package org.sang.bean

/**
 * Created by sang on 2017/12/17.
 */
class Role {
    var id: Long? = null
    var name: String? = null

    constructor() {}
    constructor(id: Long?, name: String?) {
        this.id = id
        this.name = name
    }
}