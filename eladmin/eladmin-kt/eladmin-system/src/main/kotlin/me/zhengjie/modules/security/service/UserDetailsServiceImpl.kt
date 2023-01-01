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
package me.zhengjie.modules.security.service

import lombok.RequiredArgsConstructor
import me.zhengjie.exception.BadRequestException
import me.zhengjie.exception.EntityNotFoundException
import me.zhengjie.modules.security.config.bean.LoginProperties
import me.zhengjie.modules.security.service.dto.JwtUserDto
import me.zhengjie.modules.system.service.DataService
import me.zhengjie.modules.system.service.RoleService
import me.zhengjie.modules.system.service.UserService
import me.zhengjie.modules.system.service.dto.UserDto
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@RequiredArgsConstructor
@Service("userDetailsService")
class UserDetailsServiceImpl : UserDetailsService {
    private val userService: UserService? = null
    private val roleService: RoleService? = null
    private val dataService: DataService? = null
    private val loginProperties: LoginProperties? = null
    fun setEnableCache(enableCache: Boolean) {
        loginProperties.setCacheEnable(enableCache)
    }

    override fun loadUserByUsername(username: String): JwtUserDto {
        var searchDb = true
        var jwtUserDto: JwtUserDto? = null
        if (loginProperties!!.isCacheEnable && userDtoCache.containsKey(username)) {
            jwtUserDto = userDtoCache[username]
            // 检查dataScope是否修改
            val dataScopes = jwtUserDto.getDataScopes()
            dataScopes.clear()
            dataScopes.addAll(dataService!!.getDeptIds(jwtUserDto.getUser()))
            searchDb = false
        }
        if (searchDb) {
            val user: UserDto?
            user = try {
                userService!!.findByName(username)
            } catch (e: EntityNotFoundException) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw UsernameNotFoundException("", e)
            }
            if (user == null) {
                throw UsernameNotFoundException("")
            } else {
                if (!user.enabled) {
                    throw BadRequestException("账号未激活！")
                }
                jwtUserDto = JwtUserDto(
                    user,
                    dataService!!.getDeptIds(user),
                    roleService!!.mapToGrantedAuthorities(user)
                )
                userDtoCache[username] = jwtUserDto
            }
        }
        return jwtUserDto!!
    }

    companion object {
        /**
         * 用户信息缓存
         *
         * @see UserCacheClean
         */
        var userDtoCache: MutableMap<String?, JwtUserDto> = ConcurrentHashMap()
    }
}