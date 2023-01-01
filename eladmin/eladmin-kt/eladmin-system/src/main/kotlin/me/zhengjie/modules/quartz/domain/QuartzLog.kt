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
package me.zhengjie.modules.quartz.domain

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Entity
@Data
@Table(name = "sys_quartz_log")
class QuartzLog : Serializable {
    @Id
    @Column(name = "log_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Schema(name = "任务名称", hidden = true)
    private val jobName: String? = null

    @Schema(name = "bean名称", hidden = true)
    private val beanName: String? = null

    @Schema(name = "方法名称", hidden = true)
    private val methodName: String? = null

    @Schema(name = "参数", hidden = true)
    private val params: String? = null

    @Schema(name = "cron表达式", hidden = true)
    private val cronExpression: String? = null

    @Schema(name = "状态", hidden = true)
    private val isSuccess: Boolean? = null

    @Schema(name = "异常详情", hidden = true)
    private val exceptionDetail: String? = null

    @Schema(name = "执行耗时", hidden = true)
    private val time: Long? = null

    @CreationTimestamp
    @Schema(name = "创建时间", hidden = true)
    private val createTime: Timestamp? = null
}