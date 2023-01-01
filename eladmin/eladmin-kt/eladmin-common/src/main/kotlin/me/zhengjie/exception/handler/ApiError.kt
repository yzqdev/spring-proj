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
package me.zhengjie.exception.handler

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data
import java.time.LocalDateTime

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
class ApiError private constructor() {
    private val status = 400

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private val timestamp: LocalDateTime
    private val message: String? = null

    init {
        timestamp = LocalDateTime.now()
    }

    companion object {
        fun error(message: String?): ApiError {
            val apiError = ApiError()
            apiError.setMessage(message)
            return apiError
        }

        fun error(status: Int?, message: String?): ApiError {
            val apiError = ApiError()
            apiError.setStatus(status)
            apiError.setMessage(message)
            return apiError
        }
    }
}