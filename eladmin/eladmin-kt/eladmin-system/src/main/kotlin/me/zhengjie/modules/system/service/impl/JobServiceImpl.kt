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
package me.zhengjie.modules.system.service.impl

import lombok.RequiredArgsConstructor
import me.zhengjie.exception.BadRequestException
import me.zhengjie.exception.EntityExistException
import me.zhengjie.modules.system.domain.Job
import me.zhengjie.modules.system.repository.JobRepository
import me.zhengjie.modules.system.repository.UserRepository
import me.zhengjie.modules.system.service.JobService
import me.zhengjie.modules.system.service.dto.*
import me.zhengjie.modules.system.service.mapstruct.JobMapper
import me.zhengjie.utils.*
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-03-29
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["job"])
class JobServiceImpl : JobService {
    private val jobRepository: JobRepository? = null
    private val jobMapper: JobMapper? = null
    private val redisUtils: RedisUtils? = null
    private val userRepository: UserRepository? = null
    override fun queryAll(criteria: JobQueryCriteria?, pageable: Pageable?): Map<String?, Any?> {
        val page = jobRepository!!.findAll(
            { root: Root<Job>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable!!
        )
        return toPage(page.map { entity: Job -> jobMapper!!.toDto(entity) }.content, page.totalElements)
    }

    override fun queryAll(criteria: JobQueryCriteria?): List<JobDto?>? {
        val list =
            jobRepository!!.findAll { root: Root<Job>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }
        return jobMapper!!.toDto(list)
    }

    @Cacheable(key = "'id:' + #p0")
    override fun findById(id: Long): JobDto? {
        val job = jobRepository!!.findById(id).orElseGet { Job() }
        isNull(job.id, "Job", "id", id)
        return jobMapper!!.toDto(job)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Job) {
        val job = jobRepository!!.findByName(resources.name)
        if (job != null) {
            throw EntityExistException(Job::class.java, "name", resources.name)
        }
        jobRepository.save(resources)
    }

    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Job) {
        val job = jobRepository!!.findById(resources.id).orElseGet { Job() }
        val old = jobRepository.findByName(resources.name)
        if (old != null && old.id != resources.id) {
            throw EntityExistException(Job::class.java, "name", resources.name)
        }
        isNull(job.id, "Job", "id", resources.id)
        resources.id = job.id
        jobRepository.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long?>?) {
        jobRepository!!.deleteAllByIdIn(ids)
        // 删除缓存
        redisUtils!!.delByKeys(CacheKey.JOB_ID, ids!!)
    }

    @Throws(IOException::class)
    override fun download(jobDtos: List<JobDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (jobDTO in jobDtos) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["岗位名称"] = jobDTO.name
            map["岗位状态"] = if (jobDTO.enabled) "启用" else "停用"
            map["创建日期"] = jobDTO.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    override fun verification(ids: Set<Long?>?) {
        if (userRepository!!.countByJobs(ids) > 0) {
            throw BadRequestException("所选的岗位中存在用户关联，请解除关联再试！")
        }
    }
}