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

import lombok.extern.slf4j.Slf4j
import me.zhengjie.exception.BadRequestException
import me.zhengjie.exception.EntityExistException
import me.zhengjie.exception.EntityNotFoundException
import me.zhengjie.utils.ThrowableUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable::class)
    fun handleException(e: Throwable): ResponseEntity<ApiError> {
        // 打印堆栈信息
        GlobalExceptionHandler.log.error(ThrowableUtil.getStackTrace(e))
        return buildResponseEntity(ApiError.Companion.error(e.message))
    }

    /**
     * BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(e: BadCredentialsException): ResponseEntity<ApiError> {
        // 打印堆栈信息
        val message = if ("坏的凭证" == e.message) "用户名或密码不正确" else e.message!!
        GlobalExceptionHandler.log.error(message)
        return buildResponseEntity(ApiError.Companion.error(message))
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = [BadRequestException::class])
    fun badRequestException(e: BadRequestException): ResponseEntity<ApiError> {
        // 打印堆栈信息
        GlobalExceptionHandler.log.error(ThrowableUtil.getStackTrace(e))
        return buildResponseEntity(ApiError.Companion.error(e.status, e.message))
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = [EntityExistException::class])
    fun entityExistException(e: EntityExistException): ResponseEntity<ApiError> {
        // 打印堆栈信息
        GlobalExceptionHandler.log.error(ThrowableUtil.getStackTrace(e))
        return buildResponseEntity(ApiError.Companion.error(e.message))
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun entityNotFoundException(e: EntityNotFoundException): ResponseEntity<ApiError> {
        // 打印堆栈信息
        GlobalExceptionHandler.log.error(ThrowableUtil.getStackTrace(e))
        return buildResponseEntity(ApiError.Companion.error(HttpStatus.NOT_FOUND.value(), e.message))
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        // 打印堆栈信息
        GlobalExceptionHandler.log.error(ThrowableUtil.getStackTrace(e))
        val str = Objects.requireNonNull(e.bindingResult.allErrors[0].codes)[1].split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        var message = e.bindingResult.allErrors[0].defaultMessage
        val msg = "不能为空"
        if (msg == message) {
            message = str[1] + ":" + message
        }
        return buildResponseEntity(ApiError.Companion.error(message))
    }

    /**
     * 统一返回
     */
    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<ApiError> {
        return ResponseEntity(apiError, HttpStatus.valueOf(apiError.status))
    }
}