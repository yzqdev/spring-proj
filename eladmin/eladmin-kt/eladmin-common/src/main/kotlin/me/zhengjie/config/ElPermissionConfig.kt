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
package me.zhengjie.config

import me.zhengjie.utils.SecurityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Zheng Jie
 */
@Service(value = "el")
class ElPermissionConfig {
    fun check(vararg permissions: String): Boolean {
        // 获取当前用户的所有权限
        val elPermissions =
            SecurityUtils.getCurrentUser().authorities.stream().map { obj: GrantedAuthority -> obj.authority }
                .toList()
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("admin") || Arrays.stream(permissions)
            .anyMatch { o: String -> elPermissions.contains(o) }
    }
}