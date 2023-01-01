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
package me.zhengjie.modules.quartz.service.impl

import cn.hutool.core.util.IdUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.quartz.domain.QuartzJob
import me.zhengjie.modules.quartz.domain.QuartzLog
import me.zhengjie.modules.quartz.repository.QuartzJobRepository
import me.zhengjie.modules.quartz.repository.QuartzLogRepository
import me.zhengjie.modules.quartz.service.QuartzJobService
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria
import me.zhengjie.modules.quartz.utils.QuartzManage
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.RedisUtils
import me.zhengjie.utils.StringUtils
import me.zhengjie.utils.ValidationUtil.isNull
import org.quartz.*
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@RequiredArgsConstructor
@Service(value = "quartzJobService")
class QuartzJobServiceImpl : QuartzJobService {
    private val quartzJobRepository: QuartzJobRepository? = null
    private val quartzLogRepository: QuartzLogRepository? = null
    private val quartzManage: QuartzManage? = null
    private val redisUtils: RedisUtils? = null
    override fun queryAll(criteria: JobQueryCriteria?, pageable: Pageable?): Any {
        return toPage(quartzJobRepository.findAll(Specification { root: Root<QuartzJob>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }, pageable))
    }

    override fun queryAllLog(criteria: JobQueryCriteria?, pageable: Pageable?): Any {
        return toPage(quartzLogRepository.findAll(Specification { root: Root<QuartzLog>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }, pageable))
    }

    override fun queryAll(criteria: JobQueryCriteria?): List<QuartzJob?> {
        return quartzJobRepository.findAll(Specification { root: Root<QuartzJob>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        })
    }

    override fun queryAllLog(criteria: JobQueryCriteria?): List<QuartzLog?> {
        return quartzLogRepository.findAll(Specification { root: Root<QuartzLog>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        })
    }

    override fun findById(id: Long): QuartzJob {
        val quartzJob = quartzJobRepository!!.findById(id).orElseGet { QuartzJob() }!!
        isNull(quartzJob.id, "QuartzJob", "id", id)
        return quartzJob
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: QuartzJob) {
        var resources = resources
        if (!CronExpression.isValidExpression(resources.cronExpression)) {
            throw BadRequestException("cron表达式格式错误")
        }
        resources = quartzJobRepository!!.save(resources)
        quartzManage!!.addJob(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: QuartzJob) {
        var resources = resources
        if (!CronExpression.isValidExpression(resources.cronExpression)) {
            throw BadRequestException("cron表达式格式错误")
        }
        if (StringUtils.isNotBlank(resources.subTask)) {
            val tasks = Arrays.asList(*resources.subTask.split("[,，]".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
            if (tasks.contains(resources.id.toString())) {
                throw BadRequestException("子任务中不能添加当前任务ID")
            }
        }
        resources = quartzJobRepository!!.save(resources)
        quartzManage!!.updateJobCron(resources)
    }

    override fun updateIsPause(quartzJob: QuartzJob) {
        if (quartzJob.isPause) {
            quartzManage!!.resumeJob(quartzJob)
            quartzJob.isPause = false
        } else {
            quartzManage!!.pauseJob(quartzJob)
            quartzJob.isPause = true
        }
        quartzJobRepository!!.save(quartzJob)
    }

    override fun execution(quartzJob: QuartzJob?) {
        quartzManage!!.runJobNow(quartzJob)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long>) {
        for (id in ids) {
            val quartzJob = findById(id)
            quartzManage!!.deleteJob(quartzJob)
            quartzJobRepository!!.delete(quartzJob)
        }
    }

    @Async
    @Transactional(rollbackFor = [Exception::class])
    @Throws(
        InterruptedException::class
    )
    override fun executionSubJob(tasks: Array<String>) {
        for (id in tasks) {
            val quartzJob = findById(id.toLong())
            // 执行任务
            val uuid = IdUtil.simpleUUID()
            quartzJob.uuid = uuid
            // 执行任务
            execution(quartzJob)
            // 获取执行状态，如果执行失败则停止后面的子任务执行
            var result = redisUtils!![uuid] as Boolean?
            while (result == null) {
                // 休眠5秒，再次获取子任务执行情况
                Thread.sleep(5000)
                result = redisUtils[uuid] as Boolean?
            }
            if (!result) {
                redisUtils.del(uuid)
                break
            }
        }
    }

    @Throws(IOException::class)
    override fun download(quartzJobs: List<QuartzJob?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (quartzJob in quartzJobs!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["任务名称"] = quartzJob.getJobName()
            map["Bean名称"] = quartzJob.getBeanName()
            map["执行方法"] = quartzJob.getMethodName()
            map["参数"] = quartzJob.getParams()
            map["表达式"] = quartzJob.getCronExpression()
            map["状态"] = if (quartzJob.getIsPause()) "暂停中" else "运行中"
            map["描述"] = quartzJob.getDescription()
            map["创建日期"] = quartzJob.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    @Throws(IOException::class)
    override fun downloadLog(queryAllLog: List<QuartzLog?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (quartzLog in queryAllLog!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["任务名称"] = quartzLog.getJobName()
            map["Bean名称"] = quartzLog.getBeanName()
            map["执行方法"] = quartzLog.getMethodName()
            map["参数"] = quartzLog.getParams()
            map["表达式"] = quartzLog.getCronExpression()
            map["异常详情"] = quartzLog.getExceptionDetail()
            map["耗时/毫秒"] = quartzLog.getTime()
            map["状态"] = if (quartzLog.getIsSuccess()) "成功" else "失败"
            map["创建日期"] = quartzLog.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }
}