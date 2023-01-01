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
package me.zhengjie.utils

import com.alipay.api.AlipayApiException
import com.alipay.api.internal.util.AlipaySignature
import me.zhengjie.domain.AlipayConfig
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 支付宝工具类
 * @author zhengjie
 * @date 2018/09/30 14:04:35
 */
@Component
class AlipayUtils {
    /**
     * 生成订单号
     * @return String
     */
    val orderCode: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val a = (Math.random() * 9000.0).toInt() + 1000
            println(a)
            val date = Date()
            val str = sdf.format(date)
            val split = str.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val s = split[0] + split[1] + split[2]
            val split1 = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val s1 = split1[0] + split1[1]
            val split2 = s1.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return split2[0] + split2[1] + split2[2] + a
        }

    /**
     * 校验签名
     * @param request HttpServletRequest
     * @param alipay 阿里云配置
     * @return boolean
     */
    fun rsaCheck(request: HttpServletRequest, alipay: AlipayConfig): Boolean {

        // 获取支付宝POST过来反馈信息
        val params: MutableMap<String, String> = HashMap(1)
        val requestParams = request.parameterMap
        for (o in requestParams.keys) {
            val name = o as String
            val values = requestParams[name]!!
            var valueStr = ""
            for (i in values.indices) {
                valueStr = if (i == values.size - 1) valueStr + values[i] else valueStr + values[i] + ","
            }
            params[name] = valueStr
        }
        return try {
            AlipaySignature.rsaCheckV1(
                params,
                alipay.publicKey,
                alipay.charset,
                alipay.signType
            )
        } catch (e: AlipayApiException) {
            false
        }
    }
}