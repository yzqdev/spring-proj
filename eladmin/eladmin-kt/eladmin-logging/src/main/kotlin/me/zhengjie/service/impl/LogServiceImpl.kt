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
package me.zhengjie.service.impl

import cn.hutool.core.lang.Dict
import cn.hutool.core.util.ObjectUtil
import cn.hutool.json.JSONUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.domain.Log
import me.zhengjie.repository.LogRepository
import me.zhengjie.service.LogService
import me.zhengjie.service.dto.LogQueryCriteria
import me.zhengjie.service.mapstruct.LogErrorMapper
import me.zhengjie.service.mapstruct.LogSmallMapper
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.StringUtils
import me.zhengjie.utils.StringUtils.getCityInfo
import me.zhengjie.utils.ValidationUtil.isNull
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.lang.reflect.Method
import java.util.function.Supplier
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Service
@RequiredArgsConstructor
class LogServiceImpl : LogService {
    private val logRepository: LogRepository? = null
    private val logErrorMapper: LogErrorMapper? = null
    private val logSmallMapper: LogSmallMapper? = null
    override fun queryAll(criteria: LogQueryCriteria, pageable: Pageable?): Any {
        val page =
            logRepository.findAll(Specification { root: Root<Log>?, criteriaQuery: CriteriaQuery<*>?, cb: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    cb!!
                )
            }, pageable)
        val status = "ERROR"
        return if (status == criteria.logType) {
            toPage(page.map { entity: Log -> logErrorMapper!!.toDto(entity) })
        } else page
    }

    override fun queryAll(criteria: LogQueryCriteria?): List<Log?> {
        return logRepository.findAll(Specification { root: Root<Log>?, criteriaQuery: CriteriaQuery<*>?, cb: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                cb!!
            )
        })
    }

    override fun queryAllByUser(criteria: LogQueryCriteria?, pageable: Pageable?): Any {
        val page =
            logRepository.findAll(Specification { root: Root<Log>?, criteriaQuery: CriteriaQuery<*>?, cb: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    cb!!
                )
            }, pageable)
        return toPage(page.map { entity: Log -> logSmallMapper!!.toDto(entity) })
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun save(username: String?, browser: String?, ip: String?, joinPoint: ProceedingJoinPoint, log: Log) {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val aopLog = method.getAnnotation(me.zhengjie.annotation.Log::class.java)

        // 方法路径
        val methodName = joinPoint.target.javaClass.name + "." + signature.name + "()"

        // 描述
        if (log != null) {
            log.description = aopLog.value()
        }
        assert(log != null)
        log.requestIp = ip
        log.address = getCityInfo(log.requestIp)
        log.method = methodName
        log.username = username
        log.params = getParameter(method, joinPoint.args)
        log.browser = browser
        logRepository!!.save(log)
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private fun getParameter(method: Method, args: Array<Any>): String {
        val argList: MutableList<Any> = ArrayList()
        val parameters = method.parameters
        for (i in parameters.indices) {
            //将RequestBody注解修饰的参数作为请求参数
            val requestBody = parameters[i].getAnnotation(
                RequestBody::class.java
            )
            if (requestBody != null) {
                argList.add(args[i])
            }
            //将RequestParam注解修饰的参数作为请求参数
            val requestParam = parameters[i].getAnnotation(RequestParam::class.java)
            if (requestParam != null) {
                val map: MutableMap<String, Any> = HashMap()
                var key = parameters[i].name
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value()
                }
                map[key] = args[i]
                argList.add(map)
            }
        }
        if (argList.size == 0) {
            return ""
        }
        return if (argList.size == 1) JSONUtil.toJsonStr(argList[0]) else JSONUtil.toJsonStr(argList)
    }

    override fun findByErrDetail(id: Long): Any? {
        val log = logRepository!!.findById(id).orElseGet(Supplier<Log> { Log() })!!
        isNull(log.id, "Log", "id", id)
        val details = log.exceptionDetail
        return Dict.create().set("exception", String(if (ObjectUtil.isNotNull(details)) details else "".toByteArray()))
    }

    @Throws(IOException::class)
    override fun download(logs: List<Log?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (log in logs!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["用户名"] = log.getUsername()
            map["IP"] = log.getRequestIp()
            map["IP来源"] = log.getAddress()
            map["描述"] = log.getDescription()
            map["浏览器"] = log.getBrowser()
            map["请求耗时/毫秒"] = log.getTime()
            map["异常详情"] =
                String(if (ObjectUtil.isNotNull(log.getExceptionDetail())) log.getExceptionDetail() else "".toByteArray())
            map["创建日期"] = log.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delAllByError() {
        logRepository!!.deleteByLogType("ERROR")
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delAllByInfo() {
        logRepository!!.deleteByLogType("INFO")
    }

    companion object {
        private val log = LoggerFactory.getLogger(LogServiceImpl::class.java)
    }
}