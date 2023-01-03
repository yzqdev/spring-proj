package org.javaboy.vhr.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.util.*

class Position : Serializable {
    var id: Int? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val position = o as Position
        return name == position.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    private var name: String? = null

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var createDate: Date? = null
    var enabled: Boolean? = null
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name?.trim { it <= ' ' }
    }
}