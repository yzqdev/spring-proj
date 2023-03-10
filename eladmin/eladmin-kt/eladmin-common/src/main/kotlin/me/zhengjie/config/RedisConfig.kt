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
package me.zhengjie.config

import cn.hutool.core.lang.Assert
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.ParserConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import lombok.extern.slf4j.Slf4j
import me.zhengjie.utils.StringUtils
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.Cache
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.time.Duration

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnClass(
    RedisOperations::class
)
@EnableConfigurationProperties(RedisProperties::class)
class RedisConfig : CachingConfigurerSupport() {
    /**
     * ?????? redis ?????????????????????????????????2??????
     * ??????@cacheable ???????????????
     */
    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        val fastJsonRedisSerializer = FastJsonRedisSerializer(
            Any::class.java
        )
        var configuration = RedisCacheConfiguration.defaultCacheConfig()
        configuration = configuration.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer)
        ).entryTtl(
            Duration.ofHours(6)
        )
        return configuration
    }

    @Bean(name = ["redisTemplate"])
    @ConditionalOnMissingBean(name = ["redisTemplate"])
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<Any, Any> {
        val template = RedisTemplate<Any, Any>()
        //?????????
        val fastJsonRedisSerializer = FastJsonRedisSerializer(
            Any::class.java
        )
        // value?????????????????????fastJsonRedisSerializer
        template.valueSerializer = fastJsonRedisSerializer
        template.hashValueSerializer = fastJsonRedisSerializer
        // ????????????AutoType?????????????????????????????????????????????
        ParserConfig.getGlobalInstance().isAutoTypeSupport = true
        // ???????????????????????????????????????????????????
        // ParserConfig.getGlobalInstance().addAccept("me.zhengjie.domain");
        // key??????????????????StringRedisSerializer
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.setConnectionFactory(redisConnectionFactory!!)
        return template
    }

    /**
     * ???????????????key???????????????????????????????????????
     */
    @Bean
    override fun keyGenerator(): KeyGenerator? {
        return KeyGenerator { target: Any, method: Method, params: Array<Any> ->
            val container: MutableMap<String, Any> = HashMap(3)
            val targetClassClass: Class<*> = target.javaClass
            // ?????????
            container["class"] = targetClassClass.toGenericString()
            // ????????????
            container["methodName"] = method.name
            // ?????????
            container["package"] = targetClassClass.getPackage()
            // ????????????
            for (i in params.indices) {
                container[i.toString()] = params[i]
            }
            // ??????JSON?????????
            val jsonString = JSON.toJSONString(container)
            DigestUtils.sha256Hex(jsonString)
        }
    }

    @Bean
    override fun errorHandler(): CacheErrorHandler? {
        // ??????????????????Redis??????????????????????????????????????????????????????
        RedisConfig.log.info("????????? -> [{}]", "Redis CacheErrorHandler")
        return object : CacheErrorHandler {
            override fun handleCacheGetError(e: RuntimeException, cache: Cache, key: Any) {
                RedisConfig.log.error("Redis occur handleCacheGetError???key -> [{}]", key, e)
            }

            override fun handleCachePutError(e: RuntimeException, cache: Cache, key: Any, value: Any?) {
                RedisConfig.log.error("Redis occur handleCachePutError???key -> [{}]???value -> [{}]", key, value, e)
            }

            override fun handleCacheEvictError(e: RuntimeException, cache: Cache, key: Any) {
                RedisConfig.log.error("Redis occur handleCacheEvictError???key -> [{}]", key, e)
            }

            override fun handleCacheClearError(e: RuntimeException, cache: Cache) {
                RedisConfig.log.error("Redis occur handleCacheClearError???", e)
            }
        }
    }
}

/**
 * Value ?????????
 *
 * @author /
 * @param <T>
</T> */
internal class FastJsonRedisSerializer<T>(private val clazz: Class<T>) : RedisSerializer<T?> {
    override fun serialize(t: T?): ByteArray? {
        return if (t == null) {
            ByteArray(0)
        } else JSON.toJSONString(t, SerializerFeature.WriteClassName)
            .toByteArray(StandardCharsets.UTF_8)
    }

    override fun deserialize(bytes: ByteArray?): T? {
        if (bytes == null || bytes.size <= 0) {
            return null
        }
        val str = String(bytes, StandardCharsets.UTF_8)
        return JSON.parseObject(str, clazz)
    }
}

/**
 * ??????????????????
 *
 * @author /
 */
internal class StringRedisSerializer private constructor(charset: Charset) : RedisSerializer<Any?> {
    private val charset: Charset

    constructor() : this(StandardCharsets.UTF_8) {}

    init {
        Assert.notNull(charset, "Charset must not be null!")
        this.charset = charset
    }

    override fun deserialize(bytes: ByteArray?): String? {
        return bytes?.let { String(it, charset) }
    }

    override fun serialize(`object`: Any?): ByteArray? {
        var string = JSON.toJSONString(`object`)
        if (StringUtils.isBlank(string)) {
            return null
        }
        string = string.replace("\"", "")
        return string.toByteArray(charset)
    }
}