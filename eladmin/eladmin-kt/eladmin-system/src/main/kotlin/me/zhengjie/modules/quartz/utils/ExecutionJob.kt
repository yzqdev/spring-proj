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

import cn.hutool.extra.template.TemplateEngine
import me.zhengjie.service.EmailService
import me.zhengjie.utils.StringUtils
import me.zhengjie.utils.ThrowableUtil
import org.springframework.scheduling.annotation.Async
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 * @author /
 * @date 2019-01-07
 */
@Async
class ExecutionJob : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val quartzJob: QuartzJob = context.getMergedJobDataMap().get(QuartzJob.Companion.JOB_KEY) as QuartzJob
        // 获取spring bean
        val quartzLogRepository: QuartzLogRepository = SpringContextHolder.getBean<QuartzLogRepository>(
            QuartzLogRepository::class.java
        )
        val quartzJobService: QuartzJobService =
            SpringContextHolder.getBean<QuartzJobService>(QuartzJobService::class.java)
        val redisUtils: RedisUtils = SpringContextHolder.getBean<RedisUtils>(RedisUtils::class.java)
        val uuid: String = quartzJob.getUuid()
        val log = QuartzLog()
        log.setJobName(quartzJob.getJobName())
        log.setBeanName(quartzJob.getBeanName())
        log.setMethodName(quartzJob.getMethodName())
        log.setParams(quartzJob.getParams())
        val startTime = System.currentTimeMillis()
        log.setCronExpression(quartzJob.getCronExpression())
        try {
            // 执行任务
            println("--------------------------------------------------------------")
            println("任务开始执行，任务名称：" + quartzJob.getJobName())
            val task = QuartzRunnable(
                quartzJob.getBeanName(), quartzJob.getMethodName(),
                quartzJob.getParams()
            )
            val future: Future<*> = EXECUTOR.submit(task)
            future.get()
            val times = System.currentTimeMillis() - startTime
            log.setTime(times)
            if (StringUtils.isNotBlank(uuid)) {
                redisUtils.set(uuid, true)
            }
            // 任务状态
            log.setIsSuccess(true)
            println("任务执行完毕，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒")
            println("--------------------------------------------------------------")
            // 判断是否存在子任务
            if (StringUtils.isNotBlank(quartzJob.getSubTask())) {
                val tasks: Array<String> = quartzJob.getSubTask().split("[,，]".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                // 执行子任务
                quartzJobService.executionSubJob(tasks)
            }
        } catch (e: Exception) {
            if (StringUtils.isNotBlank(uuid)) {
                redisUtils.set(uuid, false)
            }
            println("任务执行失败，任务名称：" + quartzJob.getJobName())
            println("--------------------------------------------------------------")
            val times = System.currentTimeMillis() - startTime
            log.setTime(times)
            // 任务状态 0：成功 1：失败
            log.setIsSuccess(false)
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e))
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false)
                //更新状态
                quartzJobService.updateIsPause(quartzJob)
            }
            if (quartzJob.getEmail() != null) {
                val emailService: EmailService = SpringContextHolder.getBean(EmailService::class.java)
                // 邮箱报警
                if (StringUtils.isNoneBlank(quartzJob.getEmail())) {
                    val emailVo: EmailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e))
                    emailService.send(emailVo, emailService.find())
                }
            }
        } finally {
            quartzLogRepository.save<QuartzLog>(log)
        }
    }

    private fun taskAlarm(quartzJob: QuartzJob, msg: String): EmailVo {
        val emailVo = EmailVo()
        emailVo.setSubject("定时任务【" + quartzJob.getJobName() + "】执行失败，请尽快处理！")
        val data: MutableMap<String?, Any?> = HashMap(16)
        data["task"] = quartzJob
        data["msg"] = msg
        val engine: TemplateEngine =
            TemplateUtil.createEngine(TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH))
        val template = engine.getTemplate("email/taskAlarm.ftl")
        emailVo.setContent(template.render(data))
        val emails = Arrays.asList<String>(*quartzJob.getEmail().split("[,，]".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        emailVo.setTos(emails)
        return emailVo
    }

    companion object {
        /** 该处仅供参考  */
        private val EXECUTOR: ThreadPoolExecutor = ThreadPoolExecutorUtil.poll
    }
}