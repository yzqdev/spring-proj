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

import lombok.RequiredArgsConstructor
import me.zhengjie.modules.quartz.repository.QuartzJobRepository
import me.zhengjie.modules.quartz.utils.QuartzManage
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Component
@RequiredArgsConstructor
class JobRunner : ApplicationRunner {
    private val quartzJobRepository: QuartzJobRepository? = null
    private val quartzManage: QuartzManage? = null

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    override fun run(applicationArguments: ApplicationArguments) {
        log.info("--------------------注入定时任务---------------------")
        //List<QuartzJob> quartzJobs = quartzJobRepository.findByIsPauseIsFalse();
        //quartzJobs.forEach(quartzManage::addJob);
        log.info("--------------------定时任务注入完成---------------------")
    }

    companion object {
        private val log = LoggerFactory.getLogger(JobRunner::class.java)
    }
}