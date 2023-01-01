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
package me.zhengjie.domain

import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Entity
@Getter
@Setter
@Table(name = "sys_log")
@NoArgsConstructor
class Log(
    /** 日志类型  */
    private val logType: String,
    /** 请求耗时  */
    private val time: Long
) : Serializable {
    @Id
    @Column(name = "log_id")
    @GenericGenerator(name = "idGenerator", strategy = "me.zhengjie.utils.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private val id: Long? = null

    /** 操作用户  */
    private val username: String? = null

    /** 描述  */
    private val description: String? = null

    /** 方法名  */
    private val method: String? = null

    /** 参数  */
    private val params: String? = null

    /** 请求ip  */
    private val requestIp: String? = null

    /** 地址  */
    private val address: String? = null

    /** 浏览器   */
    private val browser: String? = null

    /** 异常详细   */
    private val exceptionDetail: ByteArray

    /** 创建日期  */
    @CreationTimestamp
    private val createTime: Timestamp? = null
}