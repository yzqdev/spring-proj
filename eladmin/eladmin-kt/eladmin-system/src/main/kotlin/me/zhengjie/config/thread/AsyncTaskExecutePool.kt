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

import lombok.extern.slf4j.Slf4j
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

/**
 * 异步任务线程池装配类
 * @author https://juejin.im/entry/5abb8f6951882555677e9da2
 * @date 2019年10月31日15:06:18
 */
@Slf4j
@Configuration
class AsyncTaskExecutePool(
    /** 注入配置类  */
    private val config: AsyncTaskProperties
) : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        //核心线程池大小
        executor.corePoolSize = config.corePoolSize
        //最大线程数
        executor.maxPoolSize = config.maxPoolSize
        //队列容量
        executor.queueCapacity = config.queueCapacity
        //活跃时间
        executor.keepAliveSeconds = config.keepAliveSeconds
        //线程名字前缀
        executor.setThreadNamePrefix("el-async-")
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return AsyncUncaughtExceptionHandler { throwable: Throwable, method: Method, objects: Array<Any?>? ->
            AsyncTaskExecutePool.log.error("====" + throwable.message + "====", throwable)
            AsyncTaskExecutePool.log.error("exception method:" + method.name)
        }
    }
}