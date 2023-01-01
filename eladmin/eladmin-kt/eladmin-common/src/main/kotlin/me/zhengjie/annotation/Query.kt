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
package me.zhengjie.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author Zheng Jie
 * @date 2019-6-4 13:52:30
 */
@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class Query( // Dong ZhaoYang 2017/8/7 基本对象的属性名
    val propName: String = "",  // Dong ZhaoYang 2017/8/7 查询方式
    val type: Type = Type.EQUAL,
    /**
     * 连接查询的属性名，如User类中的dept
     */
    val joinName: String = "",
    /**
     * 默认左连接
     */
    val join: Join = Join.LEFT,
    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     */
    val blurry: String = ""
) {
    enum class Type {
        // jie 2019/6/4 相等
        EQUAL, GREATER_THAN, LESS_THAN, INNER_LIKE, LEFT_LIKE, RIGHT_LIKE, LESS_THAN_NQ, IN, NOT_IN, NOT_EQUAL, BETWEEN, NOT_NULL, IS_NULL
    }

    /**
     * @author Zheng Jie
     * 适用于简单连接查询，复杂的请自定义该注解，或者使用sql查询
     */
    enum class Join {
        /** jie 2019-6-4 13:18:30  */
        LEFT, RIGHT, INNER
    }
}