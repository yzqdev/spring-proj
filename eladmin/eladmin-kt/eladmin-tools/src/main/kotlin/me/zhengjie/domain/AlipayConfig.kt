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
 * 支付宝配置类
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Data
@Entity
@Table(name = "tool_alipay_config")
class AlipayConfig : Serializable {
    @Id
    @Column(name = "config_id")
    @Schema(name = "ID", hidden = true)
    private val id: Long? = null

    @Schema(name = "应用ID")
    private val appId: @NotBlank String? = null

    @Schema(name = "商户私钥")
    private val privateKey: @NotBlank String? = null

    @Schema(name = "支付宝公钥")
    private val publicKey: @NotBlank String? = null

    @Schema(name = "签名方式")
    private val signType = "RSA2"

    @Column(name = "gateway_url")
    @Schema(name = "支付宝开放安全地址", hidden = true)
    private val gatewayUrl = "https://openapi.alipaydev.com/gateway.do"

    @Schema(name = "编码", hidden = true)
    private val charset = "utf-8"

    @Schema(name = "异步通知地址")
    private val notifyUrl: @NotBlank String? = null

    @Schema(name = "订单完成后返回的页面")
    private val returnUrl: @NotBlank String? = null

    @Schema(name = "类型")
    private val format = "JSON"

    @Schema(name = "商户号")
    private val sysServiceProviderId: @NotBlank String? = null
}