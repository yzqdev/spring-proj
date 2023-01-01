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
package me.zhengjie.utils

import org.apache.commons.configuration2.Configuration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.apache.commons.configuration2.ex.ConfigurationException
import org.slf4j.LoggerFactory
import java.io.File

/**
 * sql字段转java
 *
 * @author Zheng Jie
 * @date 2019-01-03
 */
object ColUtil {
    private val log = LoggerFactory.getLogger(ColUtil::class.java)

    /**
     * 转换mysql数据类型为java数据类型
     *
     * @param type 数据库字段类型
     * @return String
     */
    fun cloToJava(type: String?): String? {
        val configs = Configurations()
        try {
            val config: Configuration = configs.properties(File("generator.properties"))
            return config.getString(type, "unknowType")
        } catch (e: ConfigurationException) {
            log.error(e.message, e)
        }
        return null
    }
}