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
package me.zhengjie.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 支付宝配置类
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Data
@Entity
@Table(name = "tool_alipay_config")
public class AlipayConfig implements Serializable {

    @Id
    @Column(name = "config_id")
    @Schema(name = "ID", hidden = true)
    private Long id;

    @NotBlank
    @Schema(name = "应用ID")
    private String appId;

    @NotBlank
    @Schema(name = "商户私钥")
    private String privateKey;

    @NotBlank
    @Schema(name = "支付宝公钥")
    private String publicKey;

    @Schema(name = "签名方式")
    private String signType="RSA2";

    @Column(name = "gateway_url")
    @Schema(name = "支付宝开放安全地址", hidden = true)
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    @Schema(name = "编码", hidden = true)
    private String charset= "utf-8";

    @NotBlank
    @Schema(name = "异步通知地址")
    private String notifyUrl;

    @NotBlank
    @Schema(name = "订单完成后返回的页面")
    private String returnUrl;

    @Schema(name = "类型")
    private String format="JSON";

    @NotBlank
    @Schema(name = "商户号")
    private String sysServiceProviderId;

}
