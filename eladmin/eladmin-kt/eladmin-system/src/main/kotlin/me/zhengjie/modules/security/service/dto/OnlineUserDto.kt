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

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.util.*

/**
 * 在线用户
 * @author Zheng Jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class OnlineUserDto {
    /**
     * 用户名
     */
    private val userName: String? = null

    /**
     * 昵称
     */
    private val nickName: String? = null

    /**
     * 岗位
     */
    private val dept: String? = null

    /**
     * 浏览器
     */
    private val browser: String? = null

    /**
     * IP
     */
    private val ip: String? = null

    /**
     * 地址
     */
    private val address: String? = null

    /**
     * token
     */
    private val key: String? = null

    /**
     * 登录时间
     */
    private val loginTime: Date? = null
}