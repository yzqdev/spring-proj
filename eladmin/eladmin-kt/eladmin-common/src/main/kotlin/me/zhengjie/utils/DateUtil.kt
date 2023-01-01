/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zhengjie.utils

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author: liaojinlong
 * @date: 2020/6/11 16:28
 * @apiNote: JDK 8  新日期类 格式化与字符串转换 工具类
 */
class DateUtil {
    /**
     * 日期格式化 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    fun localDateTimeFormatyMd(localDateTime: LocalDateTime?): String {
        return DFY_MD.format(localDateTime)
    }

    companion object {
        val DFY_MD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val DFY_MD = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        /**
         * LocalDateTime 转时间戳
         *
         * @param localDateTime /
         * @return /
         */
        fun getTimeStamp(localDateTime: LocalDateTime): Long {
            return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        }

        /**
         * 时间戳转LocalDateTime
         *
         * @param timeStamp /
         * @return /
         */
        @JvmStatic
        fun fromTimeStamp(timeStamp: Long?): LocalDateTime {
            return LocalDateTime.ofEpochSecond(timeStamp!!, 0, OffsetDateTime.now().offset)
        }

        /**
         * LocalDateTime 转 Date
         * Jdk8 后 不推荐使用 [Date] Date
         *
         * @param localDateTime /
         * @return /
         */
        fun toDate(localDateTime: LocalDateTime): Date {
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }

        /**
         * LocalDate 转 Date
         * Jdk8 后 不推荐使用 [Date] Date
         *
         * @param localDate /
         * @return /
         */
        fun toDate(localDate: LocalDate): Date {
            return toDate(localDate.atTime(LocalTime.now(ZoneId.systemDefault())))
        }

        /**
         * Date转 LocalDateTime
         * Jdk8 后 不推荐使用 [Date] Date
         *
         * @param date /
         * @return /
         */
        @JvmStatic
        fun toLocalDateTime(date: Date): LocalDateTime {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        }

        /**
         * 日期 格式化
         *
         * @param localDateTime /
         * @param patten /
         * @return /
         */
        fun localDateTimeFormat(localDateTime: LocalDateTime?, patten: String?): String {
            val df = DateTimeFormatter.ofPattern(patten)
            return df.format(localDateTime)
        }

        /**
         * 日期 格式化
         *
         * @param localDateTime /
         * @param df /
         * @return /
         */
        fun localDateTimeFormat(localDateTime: LocalDateTime?, df: DateTimeFormatter): String {
            return df.format(localDateTime)
        }

        /**
         * 日期格式化 yyyy-MM-dd HH:mm:ss
         *
         * @param localDateTime /
         * @return /
         */
        @JvmStatic
        fun localDateTimeFormatyMdHms(localDateTime: LocalDateTime?): String {
            return DFY_MD_HMS.format(localDateTime)
        }

        /**
         * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
         *
         * @param localDateTime /
         * @return /
         */
        fun parseLocalDateTimeFormat(localDateTime: String?, pattern: String?): LocalDateTime {
            val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            return LocalDateTime.from(dateTimeFormatter.parse(localDateTime))
        }

        /**
         * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
         *
         * @param localDateTime /
         * @return /
         */
        fun parseLocalDateTimeFormat(localDateTime: String?, dateTimeFormatter: DateTimeFormatter): LocalDateTime {
            return LocalDateTime.from(dateTimeFormatter.parse(localDateTime))
        }

        /**
         * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd HH:mm:ss
         *
         * @param localDateTime /
         * @return /
         */
        fun parseLocalDateTimeFormatyMdHms(localDateTime: String?): LocalDateTime {
            return LocalDateTime.from(DFY_MD_HMS.parse(localDateTime))
        }
    }
}