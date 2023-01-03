package org.javaboy.vhr.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.util.*

class JobLevel : Serializable {
    var id: Int? = null
    var name: String? = null
    var titleLevel: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val jobLevel = o as JobLevel
        return name == jobLevel.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var createDate: Date? = null
    var enabled: Boolean? = null
}