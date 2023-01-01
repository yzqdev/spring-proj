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

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeansException
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Environment

/**
 * @author Jie
 * @date 2019-01-07
 */
@Slf4j
class SpringContextHolder : ApplicationContextAware, DisposableBean {
    override fun destroy() {
        clearHolder()
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        if (Companion.applicationContext != null) {
            SpringContextHolder.log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + Companion.applicationContext)
        }
        Companion.applicationContext = applicationContext
        if (addCallback) {
            for (callBack in CALL_BACKS) {
                callBack.executor()
            }
            CALL_BACKS.clear()
        }
        addCallback = false
    }

    companion object {
        private var applicationContext: ApplicationContext? = null
        private val CALL_BACKS: MutableList<CallBack> = ArrayList()
        private var addCallback = true

        /**
         * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
         * 在SpringContextHolder 初始化后，进行回调使用
         *
         * @param callBack 回调函数
         */
        @Synchronized
        fun addCallBacks(callBack: CallBack) {
            if (addCallback) {
                CALL_BACKS.add(callBack)
            } else {
                SpringContextHolder.log.warn("CallBack：{} 已无法添加！立即执行", callBack.callBackName)
                callBack.executor()
            }
        }

        /**
         * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
         */
        fun <T> getBean(name: String?): T {
            assertContextInjected()
            return applicationContext!!.getBean(name!!) as T
        }

        /**
         * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
         */
        @JvmStatic
        fun <T> getBean(requiredType: Class<T>): T {
            assertContextInjected()
            return applicationContext!!.getBean(requiredType)
        }

        /**
         * 获取SpringBoot 配置信息
         *
         * @param property     属性key
         * @param defaultValue 默认值
         * @param requiredType 返回类型
         * @return /
         */
        fun <T> getProperties(property: String?, defaultValue: T, requiredType: Class<T>?): T {
            var result = defaultValue
            try {
                result = getBean(Environment::class.java).getProperty(
                    property!!, requiredType
                )
            } catch (ignored: Exception) {
            }
            return result
        }

        /**
         * 获取SpringBoot 配置信息
         *
         * @param property 属性key
         * @return /
         */
        fun getProperties(property: String?): String? {
            return getProperties<String?>(property, null, String::class.java)
        }

        /**
         * 获取SpringBoot 配置信息
         *
         * @param property     属性key
         * @param requiredType 返回类型
         * @return /
         */
        fun <T> getProperties(property: String?, requiredType: Class<T?>?): T? {
            return getProperties(property, null, requiredType)
        }

        /**
         * 检查ApplicationContext不为空.
         */
        private fun assertContextInjected() {
            checkNotNull(applicationContext) { "applicationContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder或在SpringBoot启动类中注册SpringContextHolder." }
        }

        /**
         * 清除SpringContextHolder中的ApplicationContext为Null.
         */
        private fun clearHolder() {
            SpringContextHolder.log.debug(
                "清除SpringContextHolder中的ApplicationContext:"
                        + applicationContext
            )
            applicationContext = null
        }
    }
}