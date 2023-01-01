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

import org.springframework.stereotype.Component
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * 自定义线程名称
 * @author Zheng Jie
 * @date 2019年10月31日17:49:55
 */
@Component
class TheadFactoryName private constructor(name: String) : ThreadFactory {
    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String

    constructor() : this("el-pool") {}

    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        //此时namePrefix就是 name + 第几个用这个工厂创建线程池的
        namePrefix = name +
                POOL_NUMBER.getAndIncrement()
    }

    override fun newThread(r: Runnable): Thread {
        //此时线程的名字 就是 namePrefix + -thread- + 这个线程池中第几个执行的线程
        val t = Thread(
            group, r,
            namePrefix + "-thread-" + threadNumber.getAndIncrement(),
            0
        )
        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }
        return t
    }

    companion object {
        private val POOL_NUMBER = AtomicInteger(1)
    }
}