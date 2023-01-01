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

import cn.hutool.core.util.PageUtil
import org.springframework.data.domain.Page

/**
 * 分页工具
 * @author Zheng Jie
 * @date 2018-12-10
 */
object PageUtil : PageUtil() {
    /**
     * List 分页
     */
    fun toPage(page: Int, size: Int, list: List<*>): List<*> {
        val fromIndex = page * size
        val toIndex = page * size + size
        return if (fromIndex > list.size) {
            ArrayList<Any?>()
        } else if (toIndex >= list.size) {
            list.subList(fromIndex, list.size)
        } else {
            list.subList(fromIndex, toIndex)
        }
    }

    /**
     * Page 数据处理，预防redis反序列化报错
     */
    fun toPage(page: Page<*>): Map<String, Any> {
        val map: MutableMap<String, Any> = LinkedHashMap(2)
        map["content"] = page.content
        map["totalElements"] = page.totalElements
        return map
    }

    /**
     * 自定义分页
     */
    @JvmStatic
    fun toPage(`object`: Any, totalElements: Any): Map<String, Any> {
        val map: MutableMap<String, Any> = LinkedHashMap(2)
        map["content"] = `object`
        map["totalElements"] = totalElements
        return map
    }
}