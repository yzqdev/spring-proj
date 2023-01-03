package org.sang.bean

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.sql.Timestamp

/**
 * Created by sang on 2017/12/17.
 */
class User(private var username: String) : UserDetails {
    var id: Long? = null
    private var password: String? = null
    var nickname: String? = null
    private var enabled = false
    var roles: List<Role?>? = null
    var email: String? = null
    var userface: String? = null
    var regTime: Timestamp? = null

    override fun getUsername(): String {
        return username
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    @JsonIgnore
    override fun getAuthorities(): List<GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        for (role in roles!!) {
            authorities.add(SimpleGrantedAuthority("ROLE_" + role.getName()))
        }
        return authorities
    }

    fun setUsername(username: String) {
        this.username = username
    }

    override fun getPassword(): String {
        return password!!
    }

    fun setPassword(password: String?) {
        this.password = password
    }
}