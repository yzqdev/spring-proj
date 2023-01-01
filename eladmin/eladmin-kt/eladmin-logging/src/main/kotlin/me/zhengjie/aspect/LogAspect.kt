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
package me.zhengjie.aspect

import lombok.extern.slf4j.Slf4j
import me.zhengjie.domain.Log
import me.zhengjie.service.LogService
import me.zhengjie.utils.RequestHolder.httpServletRequest
import me.zhengjie.utils.SecurityUtils
import me.zhengjie.utils.StringUtils.getBrowser
import me.zhengjie.utils.StringUtils.getIp
import me.zhengjie.utils.ThrowableUtil.getStackTrace
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Component
@Aspect
@Slf4j
class LogAspect(private val logService: LogService) {
    var currentTime = ThreadLocal<Long>()

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(me.zhengjie.annotation.Log)")
    fun logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any {
        val result: Any
        currentTime.set(System.currentTimeMillis())
        result = joinPoint.proceed()
        val log = Log("INFO", System.currentTimeMillis() - currentTime.get())
        currentTime.remove()
        val request = httpServletRequest
        logService.save(username, getBrowser(request), getIp(request), joinPoint, log)
        return result
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    fun logAfterThrowing(joinPoint: JoinPoint, e: Throwable?) {
        val log = Log("ERROR", System.currentTimeMillis() - currentTime.get())
        currentTime.remove()
        log.exceptionDetail = getStackTrace(e!!).toByteArray()
        val request = httpServletRequest
        logService.save(username, getBrowser(request), getIp(request), joinPoint as ProceedingJoinPoint, log)
    }

    val username: String
        get() = try {
            SecurityUtils.getCurrentUsername()
        } catch (e: Exception) {
            ""
        }
}