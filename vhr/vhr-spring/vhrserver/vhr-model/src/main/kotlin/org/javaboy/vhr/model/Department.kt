package org.javaboy.vhr.model

import java.io.Serializable
import java.util.*

class Department : Serializable {
    var id: Int? = null
    private var name: String? = null
    var parentId: Int? = null

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Department
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    var depPath: String? = null
    var enabled: Boolean? = null
    var parent: Boolean? = null
    var children: List<Department> = ArrayList()
    var result: Int? = null
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name?.trim { it <= ' ' }
    }
}