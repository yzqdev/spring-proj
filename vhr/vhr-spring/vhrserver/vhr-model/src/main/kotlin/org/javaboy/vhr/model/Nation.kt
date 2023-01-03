package org.javaboy.vhr.model

import java.io.Serializable
import java.util.*

class Nation : Serializable {
    var id: Int? = null
    private var name: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val nation = o as Nation
        return name == nation.name
    }

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name?.trim { it <= ' ' }
    }
}