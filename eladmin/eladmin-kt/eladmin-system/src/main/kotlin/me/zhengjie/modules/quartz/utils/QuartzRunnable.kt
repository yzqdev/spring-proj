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
package me.zhengjie.modules.quartz.utils

import lombok.extern.slf4j.Slf4j
import me.zhengjie.utils.SpringContextHolder.Companion.getBean
import org.apache.commons.lang3.StringUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.util.concurrent.Callable

/**
 * 执行定时任务
 * @author /
 */
@Slf4j
class QuartzRunnable internal constructor(beanName: String?, methodName: String?, params: String) : Callable<Any?> {
    private val target: Any
    private var method: Method? = null
    private val params: String

    init {
        target = getBean(beanName)
        this.params = params
        if (StringUtils.isNotBlank(params)) {
            method = target.javaClass.getDeclaredMethod(methodName, String::class.java)
        } else {
            method = target.javaClass.getDeclaredMethod(methodName)
        }
    }

    @Throws(Exception::class)
    override fun call(): Any? {
        ReflectionUtils.makeAccessible(method!!)
        if (StringUtils.isNotBlank(params)) {
            method.invoke(target, params)
        } else {
            method.invoke(target)
        }
        return null
    }
}