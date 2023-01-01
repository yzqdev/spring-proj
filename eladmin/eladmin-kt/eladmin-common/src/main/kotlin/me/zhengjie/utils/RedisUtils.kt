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
package me.zhengjie.utils

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisConnectionUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * @author /
 */
@Component
class RedisUtils(private val redisTemplate: RedisTemplate<Any, Any>) {
    @Value("\${jwt.online-key}")
    private val onlineKey: String? = null

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    fun expire(key: String, time: Long): Boolean {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS)
            }
        } catch (e: Exception) {
            log.error(e.message, e)
            return false
        }
        return true
    }

    /**
     * 指定缓存失效时间
     *
     * @param key      键
     * @param time     时间(秒)
     * @param timeUnit 单位
     */
    fun expire(key: String, time: Long, timeUnit: TimeUnit?): Boolean {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit!!)
            }
        } catch (e: Exception) {
            log.error(e.message, e)
            return false
        }
        return true
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    fun getExpire(key: Any): Long {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS)
    }

    /**
     * 查找匹配key
     *
     * @param pattern key
     * @return /
     */
    fun scan(pattern: String?): List<String> {
        val options = ScanOptions.scanOptions().match(
            pattern!!
        ).build()
        val factory = redisTemplate.connectionFactory
        val rc = factory!!.connection
        val cursor = rc.scan(options)
        val result: MutableList<String> = ArrayList()
        while (cursor.hasNext()) {
            result.add(String(cursor.next()))
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory!!)
        } catch (e: Exception) {
            log.error(e.message, e)
        }
        return result
    }

    /**
     * 分页查询 key
     *
     * @param patternKey key
     * @param page       页码
     * @param size       每页数目
     * @return /
     */
    fun findKeysForPage(patternKey: String?, page: Int, size: Int): List<String> {
        val options = ScanOptions.scanOptions().match(
            patternKey!!
        ).build()
        val factory = redisTemplate.connectionFactory
        val rc = Objects.requireNonNull(factory).connection
        val cursor = rc.scan(options)
        val result: MutableList<String> = ArrayList(size)
        var tmpIndex = 0
        val fromIndex = page * size
        val toIndex = page * size + size
        while (cursor.hasNext()) {
            if (tmpIndex >= fromIndex && tmpIndex < toIndex) {
                result.add(String(cursor.next()))
                tmpIndex++
                continue
            }
            // 获取到满足条件的数据后,就可以退出了
            if (tmpIndex >= toIndex) {
                break
            }
            tmpIndex++
            cursor.next()
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory!!)
        } catch (e: Exception) {
            log.error(e.message, e)
        }
        return result
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    fun hasKey(key: String): Boolean {
        return try {
            redisTemplate.hasKey(key)
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    fun del(vararg keys: String) {
        if (keys != null && keys.size > 0) {
            if (keys.size == 1) {
                val result = redisTemplate.delete(keys[0])
                log.debug("--------------------------------------------")
                log.debug(StringBuilder("删除缓存：").append(keys[0]).append("，结果：").append(result).toString())
                log.debug("--------------------------------------------")
            } else {
                val keySet: MutableSet<Any> = HashSet()
                for (key in keys) {
                    keySet.addAll(redisTemplate.keys(key))
                }
                val count = redisTemplate.delete(keySet)
                log.debug("--------------------------------------------")
                log.debug("成功删除缓存：$keySet")
                log.debug("缓存删除数量：" + count + "个")
                log.debug("--------------------------------------------")
            }
        }
    }
    // ============================String=============================
    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    operator fun get(key: String?): Any? {
        return if (key == null) null else redisTemplate.opsForValue()[key]
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    fun multiGet(keys: List<String?>): List<Any> {
        val list = redisTemplate.opsForValue().multiGet(Sets.newHashSet<Any>(keys))
        val resultList: MutableList<*> = Lists.newArrayList<Any?>()
        Optional.ofNullable(list).ifPresent { e: List<*>? ->
            list!!.forEach(
                Consumer { ele: Any? -> Optional.ofNullable(ele).ifPresent { e: Any? -> resultList.add(e) } })
        }
        return resultList
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    operator fun set(key: String, value: Any): Boolean {
        return try {
            redisTemplate.opsForValue()[key] = value
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    operator fun set(key: String, value: Any, time: Long): Boolean {
        return try {
            if (time > 0) {
                redisTemplate.opsForValue()[key, value, time] = TimeUnit.SECONDS
            } else {
                set(key, value)
            }
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间
     * @param timeUnit 类型
     * @return true成功 false 失败
     */
    operator fun set(key: String, value: Any, time: Long, timeUnit: TimeUnit?): Boolean {
        return try {
            if (time > 0) {
                redisTemplate.opsForValue()[key, value, time] = timeUnit!!
            } else {
                set(key, value)
            }
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }
    // ================================Map=================================
    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    fun hget(key: String, item: String?): Any? {
        return redisTemplate.opsForHash<Any, Any>()[key, item!!]
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    fun hmget(key: String): Map<Any, Any> {
        return redisTemplate.opsForHash<Any, Any>().entries(key)
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    fun hmset(key: String, map: Map<String, Any>?): Boolean {
        return try {
            redisTemplate.opsForHash<Any, Any>().putAll(key, map!!)
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    fun hmset(key: String, map: Map<String, Any>?, time: Long): Boolean {
        return try {
            redisTemplate.opsForHash<Any, Any>().putAll(key, map!!)
            if (time > 0) {
                expire(key, time)
            }
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    fun hset(key: String, item: String, value: Any): Boolean {
        return try {
            redisTemplate.opsForHash<Any, Any>().put(key, item, value)
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    fun hset(key: String, item: String, value: Any, time: Long): Boolean {
        return try {
            redisTemplate.opsForHash<Any, Any>().put(key, item, value)
            if (time > 0) {
                expire(key, time)
            }
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    fun hdel(key: String, vararg item: Any?) {
        redisTemplate.opsForHash<Any, Any>().delete(key, *item)
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    fun hHasKey(key: String, item: String?): Boolean {
        return redisTemplate.opsForHash<Any, Any>().hasKey(key, item!!)
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    fun hincr(key: String, item: String, by: Double): Double {
        return redisTemplate.opsForHash<Any, Any>().increment(key, item, by)
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    fun hdecr(key: String, item: String, by: Double): Double {
        return redisTemplate.opsForHash<Any, Any>().increment(key, item, -by)
    }
    // ============================set=============================
    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    fun sGet(key: String): Set<Any>? {
        return try {
            redisTemplate.opsForSet().members(key)
        } catch (e: Exception) {
            log.error(e.message, e)
            null
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    fun sHasKey(key: String, value: Any?): Boolean {
        return try {
            redisTemplate.opsForSet().isMember(key, value)
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    fun sSet(key: String, vararg values: Any?): Long {
        return try {
            redisTemplate.opsForSet().add(key, *values)!!
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    fun sSetAndTime(key: String, time: Long, vararg values: Any?): Long {
        return try {
            val count = redisTemplate.opsForSet().add(key, *values)
            if (time > 0) {
                expire(key, time)
            }
            count!!
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    fun sGetSetSize(key: String): Long {
        return try {
            redisTemplate.opsForSet().size(key)!!
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    fun setRemove(key: String, vararg values: Any?): Long {
        return try {
            val count = redisTemplate.opsForSet().remove(key, *values)
            count!!
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }
    }
    // ===============================list=================================
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    fun lGet(key: String, start: Long, end: Long): List<Any>? {
        return try {
            redisTemplate.opsForList().range(key, start, end)
        } catch (e: Exception) {
            log.error(e.message, e)
            null
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    fun lGetListSize(key: String): Long {
        return try {
            redisTemplate.opsForList().size(key)!!
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    fun lGetIndex(key: String, index: Long): Any? {
        return try {
            redisTemplate.opsForList().index(key, index)
        } catch (e: Exception) {
            log.error(e.message, e)
            null
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    fun lSet(key: String, value: Any): Boolean {
        return try {
            redisTemplate.opsForList().rightPush(key, value)
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    fun lSet(key: String, value: Any, time: Long): Boolean {
        return try {
            redisTemplate.opsForList().rightPush(key, value)
            if (time > 0) {
                expire(key, time)
            }
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    fun lSet(key: String, value: List<Any>?): Boolean {
        return try {
            redisTemplate.opsForList().rightPushAll(key, value)
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    fun lSet(key: String, value: List<Any>?, time: Long): Boolean {
        return try {
            redisTemplate.opsForList().rightPushAll(key, value)
            if (time > 0) {
                expire(key, time)
            }
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return /
     */
    fun lUpdateIndex(key: String, index: Long, value: Any): Boolean {
        return try {
            redisTemplate.opsForList()[key, index] = value
            true
        } catch (e: Exception) {
            log.error(e.message, e)
            false
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    fun lRemove(key: String, count: Long, value: Any?): Long {
        return try {
            redisTemplate.opsForList().remove(key, count, value!!)!!
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }
    }

    /**
     * @param prefix 前缀
     * @param ids    id
     */
    fun delByKeys(prefix: String?, ids: Set<Long?>) {
        val keys: MutableSet<Any> = HashSet()
        for (id in ids) {
            keys.addAll(redisTemplate.keys(StringBuffer(prefix).append(id).toString()))
        }
        val count = redisTemplate.delete(keys)
        // 此处提示可自行删除
        log.debug("--------------------------------------------")
        log.debug("成功删除缓存：$keys")
        log.debug("缓存删除数量：" + count + "个")
        log.debug("--------------------------------------------")
    }

    companion object {
        private val log = LoggerFactory.getLogger(RedisUtils::class.java)
    }
}