package me.zhengjie.base

import lombok.Getter
import lombok.Setter
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable
import java.sql.Timestamp

/**
 * 基础DTO
 * @author Zheng Jie
 * @date 2019年10月24日20:48:53
 */
@Getter
@Setter
open class BaseDTO : Serializable {
    private val createBy: String? = null
    private val updateBy: String? = null
    private val createTime: Timestamp? = null
    private val updateTime: Timestamp? = null
    override fun toString(): String {
        val builder = ToStringBuilder(this)
        val fields = this.javaClass.declaredFields
        try {
            for (f in fields) {
                f.isAccessible = true
                builder.append(f.name, f[this]).append("\n")
            }
        } catch (e: Exception) {
            builder.append("toString builder encounter an error")
        }
        return builder.toString()
    }
}