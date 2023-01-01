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
import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseEntity
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Getter
@Setter
@Entity
@Table(name = "sys_quartz_job")
class QuartzJob : BaseEntity(), Serializable {
    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private val id: @NotNull(groups = [Update::class]) Long? = null

    @Transient
    @Schema(name = "用于子任务唯一标识", hidden = true)
    private val uuid: String? = null

    @Schema(name = "定时器名称")
    private val jobName: String? = null

    @Schema(name = "Bean名称")
    private val beanName: @NotBlank String? = null

    @Schema(name = "方法名称")
    private val methodName: @NotBlank String? = null

    @Schema(name = "参数")
    private val params: String? = null

    @Schema(name = "cron表达式")
    private val cronExpression: @NotBlank String? = null

    @Schema(name = "状态，暂时或启动")
    private val isPause = false

    @Schema(name = "负责人")
    private val personInCharge: String? = null

    @Schema(name = "报警邮箱")
    private val email: String? = null

    @Schema(name = "子任务")
    private val subTask: String? = null

    @Schema(name = "失败后暂停")
    private val pauseAfterFailure: Boolean? = null

    @Schema(name = "备注")
    private val description: @NotBlank String? = null

    companion object {
        const val JOB_KEY = "JOB_KEY"
    }
}