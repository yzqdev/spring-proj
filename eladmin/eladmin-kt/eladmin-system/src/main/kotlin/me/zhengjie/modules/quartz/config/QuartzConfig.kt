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
package me.zhengjie.modules.quartz.config

import org.quartz.Scheduler
import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.AdaptableJobFactory
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

/**
 * 定时任务配置
 * @author /
 * @date 2019-01-07
 */
@Configuration
class QuartzConfig {
    /**
     * 解决Job中注入Spring Bean为null的问题
     */
    @Component("quartzJobFactory")
    class QuartzJobFactory(private val capableBeanFactory: AutowireCapableBeanFactory) : AdaptableJobFactory() {
        @Throws(Exception::class)
        override fun createJobInstance(bundle: TriggerFiredBundle): Any {

            //调用父类的方法
            val jobInstance = super.createJobInstance(bundle)
            capableBeanFactory.autowireBean(jobInstance)
            return jobInstance
        }
    }

    /**
     * 注入scheduler到spring
     * @param quartzJobFactory /
     * @return Scheduler
     * @throws Exception /
     */
    @Bean(name = ["scheduler"])
    @Throws(Exception::class)
    fun scheduler(quartzJobFactory: QuartzJobFactory?): Scheduler {
        val factoryBean = SchedulerFactoryBean()
        factoryBean.setJobFactory(quartzJobFactory!!)
        factoryBean.afterPropertiesSet()
        val scheduler = factoryBean.scheduler
        scheduler.start()
        return scheduler
    }
}