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

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.util.ObjectUtil
import lombok.extern.slf4j.Slf4j
import me.zhengjie.annotation.DataPermission
import me.zhengjie.annotation.Query
import java.lang.reflect.Field
import java.util.*
import javax.persistence.criteria.*

/**
 * @author Zheng Jie
 * @date 2019-6-4 14:59:48
 */
@Slf4j
object QueryHelp {
    @JvmStatic
    fun <R, Q> getPredicate(root: Root<R>, query: Q?, cb: CriteriaBuilder): Predicate {
        val list: MutableList<Predicate> = ArrayList()
        if (query == null) {
            return cb.and(*list.toTypedArray())
        }
        // 数据权限验证
        val permission: DataPermission = query.javaClass.getAnnotation(DataPermission::class.java)
        if (permission != null) {
            // 获取数据权限
            val dataScopes = SecurityUtils.getCurrentUserDataScope()
            if (CollectionUtil.isNotEmpty(dataScopes)) {
                if (StringUtils.isNotBlank(permission.joinName) && StringUtils.isNotBlank(permission.fieldName)) {
                    val join: Join<*, *> = root.join<Any, Any>(permission.joinName, JoinType.LEFT)
                    list.add(getExpression<Any, R>(permission.fieldName, join, root).`in`(dataScopes))
                } else if (StringUtils.isBlank(permission.joinName) && StringUtils.isNotBlank(permission.fieldName)) {
                    list.add(getExpression<Any, R>(permission.fieldName, null, root).`in`(dataScopes))
                }
            }
        }
        try {
            val fields = getAllFields(query.javaClass, ArrayList())
            for (field in fields) {
                val accessible = field.isAccessible
                // 设置对象的访问权限，保证对private的属性的访
                field.isAccessible = true
                val q = field.getAnnotation(Query::class.java)
                if (q != null) {
                    val propName = q.propName
                    val joinName = q.joinName
                    val blurry = q.blurry
                    val attributeName = if (isBlank(propName)) field.name else propName
                    val fieldType = field.type
                    val `val` = field[query]
                    if (ObjectUtil.isNull(`val`) || "" == `val`) {
                        continue
                    }
                    var join: Join<*, *>? = null
                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        val blurrys = blurry.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val orPredicate: MutableList<Predicate> = ArrayList()
                        for (s in blurrys) {
                            orPredicate.add(
                                cb.like(
                                    root.get<Any>(s)
                                        .`as`(String::class.java), "%$`val`%"
                                )
                            )
                        }
                        val p = arrayOfNulls<Predicate>(orPredicate.size)
                        list.add(cb.or(*orPredicate.toArray(p)))
                        continue
                    }
                    if (ObjectUtil.isNotEmpty(joinName)) {
                        val joinNames = joinName.split(">".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (name in joinNames) {
                            when (q.join) {
                                Query.Join.LEFT -> if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(`val`)) {
                                    join = join!!.join(name, JoinType.LEFT)
                                } else {
                                    join = root.join<Any, Any>(name, JoinType.LEFT)
                                }

                                Query.Join.RIGHT -> if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(`val`)) {
                                    join = join!!.join(name, JoinType.RIGHT)
                                } else {
                                    join = root.join<Any, Any>(name, JoinType.RIGHT)
                                }

                                Query.Join.INNER -> if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(`val`)) {
                                    join = join!!.join(name, JoinType.INNER)
                                } else {
                                    join = root.join<Any, Any>(name, JoinType.INNER)
                                }

                                else -> {}
                            }
                        }
                    }
                    when (q.type) {
                        Query.Type.EQUAL -> list.add(
                            cb.equal(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(fieldType as Class<out Comparable<*>?>), `val`
                            )
                        )

                        Query.Type.GREATER_THAN -> list.add(
                            cb.greaterThanOrEqualTo<Comparable<*>>(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(fieldType as Class<out Comparable<*>?>), `val` as Comparable<*>
                            )
                        )

                        Query.Type.LESS_THAN -> list.add(
                            cb.lessThanOrEqualTo<Comparable<*>>(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(fieldType as Class<out Comparable<*>?>), `val` as Comparable<*>
                            )
                        )

                        Query.Type.LESS_THAN_NQ -> list.add(
                            cb.lessThan<Comparable<*>>(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(fieldType as Class<out Comparable<*>?>), `val` as Comparable<*>
                            )
                        )

                        Query.Type.INNER_LIKE -> list.add(
                            cb.like(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(String::class.java), "%$`val`%"
                            )
                        )

                        Query.Type.LEFT_LIKE -> list.add(
                            cb.like(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(String::class.java), "%$`val`"
                            )
                        )

                        Query.Type.RIGHT_LIKE -> list.add(
                            cb.like(
                                getExpression<Any, R>(attributeName, join, root)
                                    .`as`(String::class.java), "$`val`%"
                            )
                        )

                        Query.Type.IN -> if (CollUtil.isNotEmpty(`val` as Collection<Any?>)) {
                            list.add(getExpression<Any, R>(attributeName, join, root).`in`(`val`))
                        }

                        Query.Type.NOT_IN -> if (CollUtil.isNotEmpty(`val` as Collection<Any?>)) {
                            list.add(getExpression<Any, R>(attributeName, join, root).`in`(`val`).not())
                        }

                        Query.Type.NOT_EQUAL -> list.add(
                            cb.notEqual(
                                getExpression<Any, R>(attributeName, join, root),
                                `val`
                            )
                        )

                        Query.Type.NOT_NULL -> list.add(cb.isNotNull(getExpression<Any, R>(attributeName, join, root)))
                        Query.Type.IS_NULL -> list.add(cb.isNull(getExpression<Any, R>(attributeName, join, root)))
                        Query.Type.BETWEEN -> {
                            val between: List<Any> = ArrayList(`val` as List<Any>)
                            list.add(
                                cb.between<Comparable<*>>(
                                    getExpression<Any, R>(attributeName, join, root).`as`(
                                        between[0].javaClass as Class<out Comparable<*>?>
                                    ),
                                    between[0] as Comparable<*>, between[1] as Comparable<*>
                                )
                            )
                        }

                        else -> {}
                    }
                }
                field.isAccessible = accessible
            }
        } catch (e: Exception) {
            QueryHelp.log.error(e.message, e)
        }
        val size = list.size
        return cb.and(*list.toTypedArray())
    }

    private fun <T, R> getExpression(attributeName: String, join: Join<*, *>?, root: Root<R>): Expression<T> {
        return if (ObjectUtil.isNotEmpty(join)) {
            join!![attributeName]
        } else {
            root.get(attributeName)
        }
    }

    private fun isBlank(cs: CharSequence?): Boolean {
        var strLen: Int
        if (cs == null || cs.length.also { strLen = it } == 0) {
            return true
        }
        for (i in 0 until strLen) {
            if (!Character.isWhitespace(cs[i])) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun getAllFields(clazz: Class<*>?, fields: MutableList<Field>): List<Field> {
        if (clazz != null) {
            fields.addAll(Arrays.asList(*clazz.declaredFields))
            getAllFields(clazz.superclass, fields)
        }
        return fields
    }
}