package org.javaboy.vhr.model

import java.io.Serializable
import java.util.*

class Politicsstatus : Serializable {
    var id: Int? = null
    private var name: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Politicsstatus
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name?.trim { it <= ' ' }
    }
}