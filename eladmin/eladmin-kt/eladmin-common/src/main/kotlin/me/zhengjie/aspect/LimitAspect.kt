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

import com.google.common.collect.ImmutableList
import me.zhengjie.annotation.Limit
import me.zhengjie.exception.BadRequestException
import me.zhengjie.utils.RequestHolder
import me.zhengjie.utils.StringUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component

/**
 * @author /
 */
@Aspect
@Component
class LimitAspect(private val redisTemplate: RedisTemplate<Any, Any>) {
    @Pointcut("@annotation(me.zhengjie.annotation.Limit)")
    fun pointcut() {
    }

    @Around("pointcut()")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint): Any {
        val request = RequestHolder.httpServletRequest
        val signature = joinPoint.signature as MethodSignature
        val signatureMethod = signature.method
        val limit = signatureMethod.getAnnotation(Limit::class.java)
        val limitType = limit.limitType
        var key: String? = limit.key

        if (StringUtils.isEmpty(key)) {
            key = if (limitType == LimitType.IP) {
                StringUtils.getIp(request)
            } else {
                signatureMethod.name
            }
        }
        val keys = ImmutableList.of<Any>(
            StringUtils.join(
                limit.prefix,
                "_",
                key,
                "_",
                request.requestURI.replace("/".toRegex(), "_")
            )
        )
        val luaScript = buildLuaScript()
        val redisScript: RedisScript<Number> = DefaultRedisScript(luaScript, Number::class.java)
        val count = redisTemplate.execute(redisScript, keys, limit.count, limit.period)
        return if (null != count && count.toInt() <= limit.count) {
            logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name)
            joinPoint.proceed()
        } else {
            throw BadRequestException("访问次数受限制")
        }
    }

    /**
     * 限流脚本
     */
    private fun buildLuaScript(): String {
        return /* !!! Hit visitElement for element type: class org.jetbrains.kotlin.nj2k.tree.JKErrorExpression !!! */
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LimitAspect::class.java)
    }
}