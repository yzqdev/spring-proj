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
package me.zhengjie.service

import me.zhengjie.domain.Log
import me.zhengjie.service.dto.LogQueryCriteria
import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
interface LogService {
    /**
     * 分页查询
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    fun queryAll(criteria: LogQueryCriteria, pageable: Pageable?): Any

    /**
     * 查询全部数据
     * @param criteria 查询条件
     * @return /
     */
    fun queryAll(criteria: LogQueryCriteria?): List<Log?>

    /**
     * 查询用户日志
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return -
     */
    fun queryAllByUser(criteria: LogQueryCriteria?, pageable: Pageable?): Any

    /**
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param ip 请求IP
     * @param joinPoint /
     * @param log 日志实体
     */
    @Async
    fun save(username: String?, browser: String?, ip: String?, joinPoint: ProceedingJoinPoint, log: Log)

    /**
     * 查询异常详情
     * @param id 日志ID
     * @return Object
     */
    fun findByErrDetail(id: Long): Any?

    /**
     * 导出日志
     * @param logs 待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun download(logs: List<Log?>?, response: HttpServletResponse?)

    /**
     * 删除所有错误日志
     */
    fun delAllByError()

    /**
     * 删除所有INFO日志
     */
    fun delAllByInfo()
}