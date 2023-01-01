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
package me.zhengjie.modules.quartz.service

import me.zhengjie.modules.quartz.domain.QuartzJob
import me.zhengjie.modules.quartz.domain.QuartzLog
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria
import org.springframework.data.domain.Pageable
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
interface QuartzJobService {
    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    fun queryAll(criteria: JobQueryCriteria?, pageable: Pageable?): Any

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    fun queryAll(criteria: JobQueryCriteria?): List<QuartzJob?>

    /**
     * 分页查询日志
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    fun queryAllLog(criteria: JobQueryCriteria?, pageable: Pageable?): Any

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    fun queryAllLog(criteria: JobQueryCriteria?): List<QuartzLog?>

    /**
     * 创建
     * @param resources /
     */
    fun create(resources: QuartzJob)

    /**
     * 编辑
     * @param resources /
     */
    fun update(resources: QuartzJob)

    /**
     * 删除任务
     * @param ids /
     */
    fun delete(ids: Set<Long>)

    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    fun findById(id: Long): QuartzJob

    /**
     * 更改定时任务状态
     * @param quartzJob /
     */
    fun updateIsPause(quartzJob: QuartzJob?)

    /**
     * 立即执行定时任务
     * @param quartzJob /
     */
    fun execution(quartzJob: QuartzJob?)

    /**
     * 导出定时任务
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun download(queryAll: List<QuartzJob?>?, response: HttpServletResponse?)

    /**
     * 导出定时任务日志
     * @param queryAllLog 待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun downloadLog(queryAllLog: List<QuartzLog?>?, response: HttpServletResponse?)

    /**
     * 执行子任务
     * @param tasks /
     * @throws InterruptedException /
     */
    @Throws(InterruptedException::class)
    fun executionSubJob(tasks: Array<String>)
}