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
package me.zhengjie.service.impl

import com.alipay.api.AlipayClient
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.request.AlipayTradePagePayRequest
import com.alipay.api.request.AlipayTradeWapPayRequest
import lombok.RequiredArgsConstructor
import me.zhengjie.domain.*
import me.zhengjie.domain.vo.TradeVo
import me.zhengjie.exception.BadRequestException
import me.zhengjie.repository.AliPayRepository
import me.zhengjie.service.AliPayService
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["aliPay"])
class AliPayServiceImpl : AliPayService {
    private val alipayRepository: AliPayRepository? = null
    @Cacheable(key = "'config'")
    override fun find(): AlipayConfig {
        val alipayConfig = alipayRepository!!.findById(1L)
        return alipayConfig.orElseGet(Supplier<AlipayConfig> { AlipayConfig() })
    }

    @CachePut(key = "'config'")
    @Transactional(rollbackFor = [Exception::class])
    override fun config(alipayConfig: AlipayConfig): AlipayConfig {
        alipayConfig.id = 1L
        return alipayRepository!!.save(alipayConfig)
    }

    @Throws(Exception::class)
    override fun toPayAsPc(alipay: AlipayConfig, trade: TradeVo): String? {
        if (alipay.id == null) {
            throw BadRequestException("????????????????????????????????????")
        }
        val alipayClient: AlipayClient = DefaultAlipayClient(
            alipay.gatewayUrl,
            alipay.appId,
            alipay.privateKey,
            alipay.format,
            alipay.charset,
            alipay.publicKey,
            alipay.signType
        )

        // ??????API?????????request(???????????????)
        val request = AlipayTradePagePayRequest()

        // ???????????????????????????????????????????????????
        request.returnUrl = alipay.returnUrl
        request.notifyUrl = alipay.notifyUrl
        // ??????????????????
        request.bizContent = "{" +
                "    \"out_trade_no\":\"" + trade.outTradeNo + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.totalAmount + "," +
                "    \"subject\":\"" + trade.subject + "\"," +
                "    \"body\":\"" + trade.body + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipay.sysServiceProviderId + "\"" +
                "    }" + "  }" //??????????????????
        // ??????SDK????????????, ??????GET????????????????????????url
        return alipayClient.pageExecute(request, "GET").body
    }

    @Throws(Exception::class)
    override fun toPayAsWeb(alipay: AlipayConfig, trade: TradeVo): String? {
        if (alipay.id == null) {
            throw BadRequestException("????????????????????????????????????")
        }
        val alipayClient: AlipayClient = DefaultAlipayClient(
            alipay.gatewayUrl,
            alipay.appId,
            alipay.privateKey,
            alipay.format,
            alipay.charset,
            alipay.publicKey,
            alipay.signType
        )
        val money = trade.totalAmount.toDouble()
        val maxMoney = 5000.0
        if (money <= 0 || money >= maxMoney) {
            throw BadRequestException("??????????????????")
        }
        // ??????API?????????request(???????????????)
        val request = AlipayTradeWapPayRequest()
        request.returnUrl = alipay.returnUrl
        request.notifyUrl = alipay.notifyUrl
        request.bizContent = "{" +
                "    \"out_trade_no\":\"" + trade.outTradeNo + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.totalAmount + "," +
                "    \"subject\":\"" + trade.subject + "\"," +
                "    \"body\":\"" + trade.body + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipay.sysServiceProviderId + "\"" +
                "    }" + "  }"
        return alipayClient.pageExecute(request, "GET").body
    }
}