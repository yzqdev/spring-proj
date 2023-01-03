package org.javaboy.vhr.model

import com.baomidou.mybatisplus.annotation.IdType
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

class Hr : UserDetails {
    @TableId(type = IdType.ASSIGN_ID)
    var id: Int? = null
    var name: String? = null
        set(name) {
            field = name?.trim { it <= ' ' }
        }
    var phone: String? = null
        set(phone) {
            field = phone?.trim { it <= ' ' }
        }
    var telephone: String? = null
        set(telephone) {
            field = telephone?.trim { it <= ' ' }
        }
    var address: String? = null
        set(address) {
            field = address?.trim { it <= ' ' }
        }
    var isEnabled: Boolean? = null
    private var username: String? = null
    var password: String? = null
        set(password) {
            field = password?.trim { it <= ' ' }
        }
    var userface: String? = null
        set(userface) {
            field = userface?.trim { it <= ' ' }
        }
    var remark: String? = null
        set(remark) {
            field = remark?.trim { it <= ' ' }
        }
    var roles: List<Role>? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val hr = o as Hr
        return username == hr.username
    }

    override fun hashCode(): Int {
        return Objects.hash(username)
    }

    fun getUsername(): String? {
        return username
    }

    val isAccountNonExpired: Boolean
        get() = true
    val isAccountNonLocked: Boolean
        get() = true
    val isCredentialsNonExpired: Boolean
        get() = true

    fun setUsername(username: String?) {
        this.username = username?.trim { it <= ' ' }
    }

    @get:JsonIgnore
    val authorities: Collection<Any?>
        get() {
            val authorities: MutableList<SimpleGrantedAuthority?> = ArrayList<SimpleGrantedAuthority>(
                roles!!.size
            )
            for (role in roles!!) {
                authorities.add(SimpleGrantedAuthority(role.name))
            }
            return authorities
        }
}