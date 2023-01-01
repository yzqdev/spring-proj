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
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.quartz.domain.QuartzJob
import org.quartz.*
import org.quartz.impl.triggers.CronTriggerImpl
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.Resource

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Slf4j
@Component
class QuartzManage {
    @Resource(name = "scheduler")
    private val scheduler: Scheduler? = null
    fun addJob(quartzJob: QuartzJob?) {
        try {
            // 构建job信息
            val jobDetail =
                JobBuilder.newJob(ExecutionJob::class.java).withIdentity(JOB_NAME + quartzJob.getId()).build()

            //通过触发器名和cron 表达式创建 Trigger
            val cronTrigger: Trigger = TriggerBuilder.newTrigger()
                .withIdentity(JOB_NAME + quartzJob.getId())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
                .build()
            cronTrigger.jobDataMap[QuartzJob.Companion.JOB_KEY] = quartzJob

            //重置启动时间
            (cronTrigger as CronTriggerImpl).startTime = Date()

            //执行定时任务
            scheduler!!.scheduleJob(jobDetail, cronTrigger)

            // 暂停任务
            if (quartzJob.getIsPause()) {
                pauseJob(quartzJob)
            }
        } catch (e: Exception) {
            QuartzManage.log.error("创建定时任务失败", e)
            throw BadRequestException("创建定时任务失败")
        }
    }

    /**
     * 更新job cron表达式
     * @param quartzJob /
     */
    fun updateJobCron(quartzJob: QuartzJob) {
        try {
            val triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.id)
            var trigger: CronTrigger? = scheduler!!.getTrigger(triggerKey) as CronTrigger
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob)
                trigger = scheduler.getTrigger(triggerKey) as CronTrigger
            }
            val scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.cronExpression)
            trigger = trigger!!.triggerBuilder.withIdentity(triggerKey).withSchedule(scheduleBuilder).build()
            //重置启动时间
            (trigger as CronTriggerImpl?)!!.startTime = Date()
            trigger.getJobDataMap()[QuartzJob.Companion.JOB_KEY] = quartzJob
            scheduler.rescheduleJob(triggerKey, trigger)
            // 暂停任务
            if (quartzJob.isPause) {
                pauseJob(quartzJob)
            }
        } catch (e: Exception) {
            QuartzManage.log.error("更新定时任务失败", e)
            throw BadRequestException("更新定时任务失败")
        }
    }

    /**
     * 删除一个job
     * @param quartzJob /
     */
    fun deleteJob(quartzJob: QuartzJob) {
        try {
            val jobKey = JobKey.jobKey(JOB_NAME + quartzJob.id)
            scheduler!!.pauseJob(jobKey)
            scheduler.deleteJob(jobKey)
        } catch (e: Exception) {
            QuartzManage.log.error("删除定时任务失败", e)
            throw BadRequestException("删除定时任务失败")
        }
    }

    /**
     * 恢复一个job
     * @param quartzJob /
     */
    fun resumeJob(quartzJob: QuartzJob) {
        try {
            val triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.id)
            val trigger = scheduler!!.getTrigger(triggerKey) as CronTrigger
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob)
            }
            val jobKey = JobKey.jobKey(JOB_NAME + quartzJob.id)
            scheduler.resumeJob(jobKey)
        } catch (e: Exception) {
            QuartzManage.log.error("恢复定时任务失败", e)
            throw BadRequestException("恢复定时任务失败")
        }
    }

    /**
     * 立即执行job
     * @param quartzJob /
     */
    fun runJobNow(quartzJob: QuartzJob?) {
        try {
            val triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId())
            val trigger = scheduler!!.getTrigger(triggerKey) as CronTrigger
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob)
            }
            val dataMap = JobDataMap()
            dataMap[QuartzJob.Companion.JOB_KEY] = quartzJob
            val jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId())
            scheduler.triggerJob(jobKey, dataMap)
        } catch (e: Exception) {
            QuartzManage.log.error("定时任务执行失败", e)
            throw BadRequestException("定时任务执行失败")
        }
    }

    /**
     * 暂停一个job
     * @param quartzJob /
     */
    fun pauseJob(quartzJob: QuartzJob?) {
        try {
            val jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId())
            scheduler!!.pauseJob(jobKey)
        } catch (e: Exception) {
            QuartzManage.log.error("定时任务暂停失败", e)
            throw BadRequestException("定时任务暂停失败")
        }
    }

    companion object {
        private const val JOB_NAME = "TASK_"
    }
}