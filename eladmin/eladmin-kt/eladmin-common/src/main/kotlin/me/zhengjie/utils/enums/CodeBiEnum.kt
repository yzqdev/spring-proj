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
package me.zhengjie.utils.enums

import lombok.AllArgsConstructor
import lombok.Getter

/**
 *
 *
 * 验证码业务场景
 *
 * @author Zheng Jie
 * @date 2020-05-02
 */
@Getter
@AllArgsConstructor
enum class CodeBiEnum {
    /* 旧邮箱修改邮箱 */
    ONE(1, "旧邮箱修改邮箱"),  /* 通过邮箱修改密码 */
    TWO(2, "通过邮箱修改密码");

    private val code: Int? = null
    private val description: String? = null

    companion object {
        @JvmStatic
        fun find(code: Int): CodeBiEnum? {
            for (value in values()) {
                if (code == value.getCode()) {
                    return value
                }
            }
            return null
        }
    }
}