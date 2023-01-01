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
package me.zhengjie.modules.system.domain

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
 * @date 2019-03-29
 */
@Entity
@Getter
@Setter
@Table(name = "sys_job")
class Job : BaseEntity(), Serializable {
    @Id
    @Column(name = "job_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: @NotNull(groups = [Update::class]) Long? = null

    @Schema(name = "岗位名称")
    private val name: @NotBlank String? = null

    @Schema(name = "岗位排序")
    private val jobSort: @NotNull Long? = null

    @Schema(name = "是否启用")
    private val enabled: @NotNull Boolean? = null


}