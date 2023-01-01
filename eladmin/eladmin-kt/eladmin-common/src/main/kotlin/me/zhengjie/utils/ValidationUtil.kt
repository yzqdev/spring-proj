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

import cn.hutool.core.util.ObjectUtil
import me.zhengjie.exception.BadRequestException
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator

/**
 * 验证工具
 * @author Zheng Jie
 * @date 2018-11-23
 */
object ValidationUtil {
    /**
     * 验证空
     */
    @JvmStatic
    fun isNull(obj: Any?, entity: String, parameter: String, value: Any) {
        if (ObjectUtil.isNull(obj)) {
            val msg = "$entity 不存在: $parameter is $value"
            throw BadRequestException(msg)
        }
    }

    /**
     * 验证是否为邮箱
     */
    fun isEmail(email: String?): Boolean {
        return EmailValidator().isValid(email, null)
    }
}