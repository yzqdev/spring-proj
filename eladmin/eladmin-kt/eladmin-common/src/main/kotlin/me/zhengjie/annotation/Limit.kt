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

import me.zhengjie.aspect.LimitType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author jacky
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    RetentionPolicy.RUNTIME
)
annotation class Limit( // 资源名称，用于描述接口功能
    val name: String = "",  // 资源 key
    val key: String = "",  // key prefix
    val prefix: String = "",  // 时间的，单位秒
    val period: Int,  // 限制访问次数
    val count: Int,  // 限制类型
    val limitType: LimitType = LimitType.CUSTOMER
)