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

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank

/**
 * 邮件配置类，数据存覆盖式存入数据存
 * @author Zheng Jie
 * @date 2018-12-26
 */
@Entity
@Data
@Table(name = "tool_email_config")
class EmailConfig : Serializable {
    @Id
    @Column(name = "config_id")
    @Schema(name = "ID", hidden = true)
    private val id: Long? = null

    @Schema(name = "邮件服务器SMTP地址")
    private val host: @NotBlank String? = null

    @Schema(name = "邮件服务器 SMTP 端口")
    private val port: @NotBlank String? = null

    @Schema(name = "发件者用户名")
    private val user: @NotBlank String? = null

    @Schema(name = "密码")
    private val pass: @NotBlank String? = null

    @Schema(name = "收件人")
    private val fromUser: @NotBlank String? = null
}