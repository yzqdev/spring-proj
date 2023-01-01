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
package me.zhengjie.domain.vo

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.sql.Date
import java.sql.Timestamp
import javax.validation.constraints.NotBlank

/**
 * 交易详情，按需应该存入数据库，这里存入数据库，仅供临时测试
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Data
class TradeVo {
    /** （必填）商品描述  */
    private val body: @NotBlank String? = null

    /** （必填）商品名称  */
    private val subject: @NotBlank String? = null

    /** （必填）商户订单号，应该由后台生成  */
    @Schema(hidden = true)
    private val outTradeNo: String? = null

    /** （必填）第三方订单号  */
    @Schema(hidden = true)
    private val tradeNo: String? = null

    /** （必填）价格  */
    private val totalAmount: @NotBlank String? = null

    /** 订单状态,已支付，未支付，作废  */
    @Schema(hidden = true)
    private val state: String? = null

    /** 创建时间，存入数据库时需要  */
    @Schema(hidden = true)
    private val createTime: Timestamp? = null

    /** 作废时间，存入数据库时需要  */
    @Schema(hidden = true)
    private val cancelTime: Date? = null
}