/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.security.service.dto

import com.alibaba.fastjson.annotation.JSONField
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString
import me.zhengjie.modules.system.service.dto.UserDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Getter
@AllArgsConstructor
@ToString
class JwtUserDto : UserDetails {
    private val user: UserDto? = null
    private val dataScopes: List<Long>? = null

    @JSONField(serialize = false)
    private val authorities: List<GrantedAuthority>? = null
    val roles: Set<String>
        get() = authorities!!.stream().map { obj: GrantedAuthority -> obj.authority }.collect(Collectors.toSet())

    @JSONField(serialize = false)
    override fun getPassword(): String {
        return user!!.password
    }

    @JSONField(serialize = false)
    override fun getUsername(): String {
        return user!!.username
    }

    @JSONField(serialize = false)
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JSONField(serialize = false)
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @JSONField(serialize = false)
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JSONField(serialize = false)
    override fun isEnabled(): Boolean {
        return user!!.enabled
    }
}