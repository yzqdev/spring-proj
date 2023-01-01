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

import lombok.extern.slf4j.Slf4j
import me.zhengjie.modules.security.config.bean.SecurityProperties
import me.zhengjie.modules.security.service.dto.JwtUserDto
import me.zhengjie.modules.security.service.dto.OnlineUserDto
import me.zhengjie.utils.EncryptUtils.desDecrypt
import me.zhengjie.utils.EncryptUtils.desEncrypt
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.RedisUtils
import me.zhengjie.utils.StringUtils
import me.zhengjie.utils.StringUtils.getBrowser
import me.zhengjie.utils.StringUtils.getCityInfo
import me.zhengjie.utils.StringUtils.getIp
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019年10月26日21:56:27
 */
@Service
@Slf4j
class OnlineUserService(private val properties: SecurityProperties, private val redisUtils: RedisUtils) {
    /**
     * 保存在线用户信息
     * @param jwtUserDto /
     * @param token /
     * @param request /
     */
    fun save(jwtUserDto: JwtUserDto, token: String?, request: HttpServletRequest?) {
        val dept = jwtUserDto.user.dept.name
        val ip = getIp(request!!)
        val browser = getBrowser(request)
        val address = getCityInfo(ip)
        var onlineUserDto: OnlineUserDto? = null
        try {
            onlineUserDto = OnlineUserDto(
                jwtUserDto.username, jwtUserDto.user.nickName, dept, browser, ip, address, desEncrypt(
                    token!!
                ), Date()
            )
        } catch (e: Exception) {
            OnlineUserService.log.error(e.message, e)
        }
        redisUtils[properties.onlineKey + token, onlineUserDto!!] = properties.tokenValidityInSeconds / 1000
    }

    /**
     * 查询全部数据
     * @param filter /
     * @param pageable /
     * @return /
     */
    fun getAll(filter: String?, pageable: Pageable): Map<String, Any> {
        val onlineUserDtos = getAll(filter)
        return toPage(
            toPage(pageable.pageNumber, pageable.pageSize, onlineUserDtos),
            onlineUserDtos.size
        )
    }

    /**
     * 查询全部数据，不分页
     * @param filter /
     * @return /
     */
    fun getAll(filter: String?): List<OnlineUserDto?> {
        val keys: List<String?> = redisUtils.scan(properties.onlineKey + "*")
        Collections.reverse(keys)
        val onlineUserDtos: MutableList<OnlineUserDto?> = ArrayList()
        for (key in keys) {
            val onlineUserDto = redisUtils[key] as OnlineUserDto?
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUserDto.toString().contains(filter!!)) {
                    onlineUserDtos.add(onlineUserDto)
                }
            } else {
                onlineUserDtos.add(onlineUserDto)
            }
        }
        onlineUserDtos.sort(Comparator { o1: OnlineUserDto, o2: OnlineUserDto -> o2.loginTime.compareTo(o1.loginTime) })
        return onlineUserDtos
    }

    /**
     * 踢出用户
     * @param key /
     */
    fun kickOut(key: String) {
        var key = key
        key = properties.onlineKey + key
        redisUtils.del(key)
    }

    /**
     * 退出登录
     * @param token /
     */
    fun logout(token: String?) {
        val key = properties.onlineKey + token
        redisUtils.del(key)
    }

    /**
     * 导出
     * @param all /
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun download(all: List<OnlineUserDto?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (user in all!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["用户名"] = user.getUserName()
            map["部门"] = user.getDept()
            map["登录IP"] = user.getIp()
            map["登录地点"] = user.getAddress()
            map["浏览器"] = user.getBrowser()
            map["登录日期"] = user.getLoginTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    /**
     * 查询用户
     * @param key /
     * @return /
     */
    fun getOne(key: String?): OnlineUserDto? {
        return redisUtils[key] as OnlineUserDto?
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    fun checkLoginOnUser(userName: String, igoreToken: String?) {
        val onlineUserDtos = getAll(userName)
        if (onlineUserDtos == null || onlineUserDtos.isEmpty()) {
            return
        }
        for (onlineUserDto in onlineUserDtos) {
            if (onlineUserDto.getUserName() == userName) {
                try {
                    val token = desDecrypt(onlineUserDto.getKey())
                    if (StringUtils.isNotBlank(igoreToken) && igoreToken != token) {
                        kickOut(token)
                    } else if (StringUtils.isBlank(igoreToken)) {
                        kickOut(token)
                    }
                } catch (e: Exception) {
                    OnlineUserService.log.error("checkUser is error", e)
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     * @param username /
     */
    @Async
    @Throws(Exception::class)
    fun kickOutForUsername(username: String) {
        val onlineUsers = getAll(username)
        for (onlineUser in onlineUsers) {
            if (onlineUser.getUserName() == username) {
                val token = desDecrypt(onlineUser.getKey())
                kickOut(token)
            }
        }
    }
}