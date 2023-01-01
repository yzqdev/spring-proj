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
package me.zhengjie.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.extern.slf4j.Slf4j
import me.zhengjie.annotation.Log
import me.zhengjie.domain.AlipayConfig
import me.zhengjie.service.AliPayService
import me.zhengjie.utils.AlipayUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody
import java.nio.charset.StandardCharsets

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aliPay")
@Tag(name = "工具：支付宝管理")
class AliPayController {
    private val alipayUtils: AlipayUtils? = null
    private val alipayService: AliPayService? = null
    @GetMapping
    fun queryConfig(): ResponseEntity<AlipayConfig> {
        return ResponseEntity<Any?>(alipayService.find(), HttpStatus.OK)
    }

    @Log("配置支付宝")
    @Operation(summary = "配置支付宝")
    @PutMapping
    fun updateConfig(@Validated @RequestBody alipayConfig: AlipayConfig?): ResponseEntity<Any> {
        alipayService.config(alipayConfig)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @Log("支付宝PC网页支付")
    @Operation(summary = "PC网页支付")
    @PostMapping(value = ["/toPayAsPC"])
    @Throws(
        Exception::class
    )
    fun toPayAsPc(@Validated @RequestBody trade: TradeVo): ResponseEntity<String> {
        val aliPay: AlipayConfig = alipayService.find()
        trade.setOutTradeNo(alipayUtils!!.orderCode)
        val payUrl: String = alipayService.toPayAsPc(aliPay, trade)
        return ResponseEntity.ok<String>(payUrl)
    }

    @Log("支付宝手机网页支付")
    @Operation(summary = "手机网页支付")
    @PostMapping(value = ["/toPayAsWeb"])
    @Throws(
        Exception::class
    )
    fun toPayAsWeb(@Validated @RequestBody trade: TradeVo): ResponseEntity<String> {
        val alipay: AlipayConfig = alipayService.find()
        trade.setOutTradeNo(alipayUtils!!.orderCode)
        val payUrl: String = alipayService.toPayAsWeb(alipay, trade)
        return ResponseEntity.ok<String>(payUrl)
    }

    @AnonymousGetMapping("/return")
    @Operation(summary = "支付之后跳转的链接")
    fun returnPage(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
        val alipay: AlipayConfig = alipayService.find()
        response.setContentType("text/html;charset=" + alipay.charset)
        //内容验签，防止黑客篡改参数
        return if (alipayUtils!!.rsaCheck(request, alipay)) {
            //商户订单号
            val outTradeNo = String(
                request.getParameter("out_trade_no").toByteArray(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
            )
            //支付宝交易号
            val tradeNo = String(
                request.getParameter("trade_no").toByteArray(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
            )
            println("商户订单号$outTradeNo  第三方交易号$tradeNo")

            // 根据业务需要返回数据，这里统一返回OK
            ResponseEntity<String>("payment successful", HttpStatus.OK)
        } else {
            // 根据业务需要返回数据
            ResponseEntity<String>(HttpStatus.BAD_REQUEST)
        }
    }

    @RequestMapping("/notify")
    @AnonymousAccess
    @Operation(summary = "支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    fun notify(request: HttpServletRequest): ResponseEntity<Any> {
        val alipay: AlipayConfig = alipayService.find()
        val parameterMap: Map<String, Array<String>> = request.getParameterMap()
        //内容验签，防止黑客篡改参数
        if (alipayUtils!!.rsaCheck(request, alipay)) {
            //交易状态
            val tradeStatus = String(
                request.getParameter("trade_status").toByteArray(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
            )
            // 商户订单号
            val outTradeNo = String(
                request.getParameter("out_trade_no").toByteArray(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
            )
            //支付宝交易号
            val tradeNo = String(
                request.getParameter("trade_no").toByteArray(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
            )
            //付款金额
            val totalAmount = String(
                request.getParameter("total_amount").toByteArray(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
            )
            //验证
            if (tradeStatus == AliPayStatusEnum.SUCCESS.value || tradeStatus == AliPayStatusEnum.FINISHED.value) {
                // 验证通过后应该根据业务需要处理订单
            }
            return ResponseEntity<Any>(HttpStatus.OK)
        }
        return ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
    }
}