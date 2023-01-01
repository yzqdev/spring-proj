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
package me.zhengjie.service.impl

import lombok.RequiredArgsConstructor
import me.zhengjie.domain.GenConfig
import me.zhengjie.repository.GenConfigRepository
import me.zhengjie.service.GenConfigService
import org.springframework.stereotype.Service
import java.io.File

/**
 * @author Zheng Jie
 * @date 2019-01-14
 */
@Service
@RequiredArgsConstructor
class GenConfigServiceImpl : GenConfigService {
    private val genConfigRepository: GenConfigRepository? = null
    override fun find(tableName: String?): GenConfig {
        return genConfigRepository!!.findByTableName(tableName) ?: return GenConfig(tableName)
    }

    override fun update(tableName: String?, genConfig: GenConfig): GenConfig {
        val separator = File.separator
        val paths: Array<String>
        val symbol = "\\"
        paths = if (symbol == separator) {
            genConfig.getPath().split("\\\\")
        } else {
            genConfig.getPath().split(File.separator)
        }
        val api = StringBuilder()
        for (path in paths) {
            api.append(path)
            api.append(separator)
            if ("src" == path) {
                api.append("api")
                break
            }
        }
        genConfig.setApiPath(api.toString())
        return genConfigRepository!!.save(genConfig)
    }
}