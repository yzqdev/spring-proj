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
package me.zhengjie.config.thread

import me.zhengjie.utils.SpringContextHolder.Companion.getBean
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 用于获取自定义线程池
 * @author Zheng Jie
 * @date 2019年10月31日18:16:47
 */
object ThreadPoolExecutorUtil {
    @JvmStatic
    val poll: ThreadPoolExecutor
        get() {
            val properties = getBean(AsyncTaskProperties::class.java)
            return ThreadPoolExecutor(
                properties.corePoolSize,
                properties.maxPoolSize,
                properties.keepAliveSeconds.toLong(),
                TimeUnit.SECONDS,
                ArrayBlockingQueue(properties.queueCapacity),
                TheadFactoryName()
            )
        }
}