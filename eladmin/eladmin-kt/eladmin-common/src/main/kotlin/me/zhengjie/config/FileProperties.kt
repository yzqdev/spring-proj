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

import lombok.Data
import me.zhengjie.utils.ElAdminConstant
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*

/**
 * @author Zheng Jie
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
class FileProperties {
    /** 文件大小限制  */
    private val maxSize: Long? = null

    /** 头像大小限制  */
    private val avatarMaxSize: Long? = null
    private val mac: ElPath? = null
    private val linux: ElPath? = null
    private val windows: ElPath? = null

    /**
     * check system os
     * @return me.zhengjie.config.ElPath
     */
    val path: ElPath?
        get() {
            val os = System.getProperty("os.name")
            if (os.lowercase(Locale.getDefault()).startsWith(ElAdminConstant.WIN)) {
                return windows
            } else if (os.lowercase(Locale.getDefault()).startsWith(ElAdminConstant.MAC)) {
                return mac
            }
            return linux
        }

    @Data
    class ElPath {
        val path: String? = null
         val avatar: String? = null
    }
}