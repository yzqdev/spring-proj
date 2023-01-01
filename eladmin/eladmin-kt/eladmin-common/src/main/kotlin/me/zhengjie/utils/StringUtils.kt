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

import cn.hutool.http.HttpUtil
import cn.hutool.http.useragent.UserAgentUtil
import cn.hutool.json.JSONUtil
import me.zhengjie.utils.FileUtil.inputStreamToFile
import org.lionsoul.ip2region.DbConfig
import org.lionsoul.ip2region.DbSearcher
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Zheng Jie
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
class StringUtils : org.apache.commons.lang3.StringUtils() {
    private val log = LoggerFactory.getLogger(StringUtils::class.java)
    private var ipLocal = false
    private var file: File? = null
    private var config: DbConfig? = null
    private val SEPARATOR = '_'
    private val UNKNOWN = "unknown"

    //private static final UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer
    //        .newBuilder()
    //        .hideMatcherLoadStats()
    //        .withCache(10000)
    //        .withField(UserAgent.AGENT_NAME_VERSION)
    //        .build();
    init {
        SpringContextHolder.Companion.addCallBacks(CallBack {
            ipLocal =
                SpringContextHolder.Companion.getProperties<Boolean>("ip.local-parsing", false, Boolean::class.java)
            if (ipLocal) {
                /*
                 * 此文件为独享 ，不必关闭
                 */
                val path = "ip2region/ip2region.db"
                val name = "ip2region.db"
                try {
                    config = DbConfig()
                    file =
                        inputStreamToFile(ClassPathResource(me.zhengjie.utils.path).inputStream, me.zhengjie.utils.name)
                } catch (e: Exception) {
                    log.error(e.message, e)
                }
            }
        })
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    @JvmStatic
    fun toCamelCase(s: String?): String? {
        var s = s ?: return null
        s = s.lowercase(Locale.getDefault())
        val sb = StringBuilder(s.length)
        var upperCase = false
        for (i in 0 until s.length) {
            val c = s[i]
            if (c == SEPARATOR) {
                upperCase = true
            } else if (upperCase) {
                sb.append(c.uppercaseChar())
                upperCase = false
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    @JvmStatic
    fun toCapitalizeCamelCase(s: String?): String? {
        var s: String? = s ?: return null
        s = toCamelCase(s)
        return s!!.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1)
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    @JvmStatic
    fun toUnderScoreCase(s: String?): String? {
        if (s == null) {
            return null
        }
        val sb = StringBuilder()
        var upperCase = false
        for (i in 0 until s.length) {
            val c = s[i]
            var nextUpperCase = true
            if (i < s.length - 1) {
                nextUpperCase = Character.isUpperCase(s[i + 1])
            }
            upperCase = if (i > 0 && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR)
                }
                true
            } else {
                false
            }
            sb.append(c.lowercaseChar())
        }
        return sb.toString()
    }

    /**
     * 获取ip地址
     */
    @JvmStatic
    fun getIp(request: HttpServletRequest): String? {
        var ip = request.getHeader("x-forwarded-for")
        if (ip == null || ip.length == 0 || UNKNOWN.equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0 || UNKNOWN.equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0 || UNKNOWN.equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        val comma = ","
        val localhost = "127.0.0.1"
        if (ip!!.contains(comma)) {
            ip = ip.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        }
        if (localhost == ip) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().hostAddress
            } catch (e: UnknownHostException) {
                log.error(e.message, e)
            }
        }
        return ip
    }

    /**
     * 根据ip获取详细地址
     */
    @JvmStatic
    fun getCityInfo(ip: String?): String {
        return if (ipLocal) {
            getLocalCityInfo(ip)
        } else {
            getHttpCityInfo(ip)
        }
    }

    /**
     * 根据ip获取详细地址
     */
    fun getHttpCityInfo(ip: String?): String {
        val api = String.format(ElAdminConstant.Url.IP_URL, ip)
        val `object` = JSONUtil.parseObj(HttpUtil.get(api))
        return `object`.get("addr", String::class.java)
    }

    /**
     * 根据ip获取详细地址
     */
    fun getLocalCityInfo(ip: String?): String {
        try {
            val dataBlock = DbSearcher(config, file!!.path)
                .binarySearch(ip)
            val region = dataBlock.region
            var address = region.replace("0|", "")
            val symbol = '|'
            if (address[address.length - 1] == symbol) {
                address = address.substring(0, address.length - 1)
            }
            return if (address == ElAdminConstant.REGION) "内网IP" else address
        } catch (e: Exception) {
            log.error(e.message, e)
        }
        return ""
    }

    @JvmStatic
    fun getBrowser(request: HttpServletRequest): String {
        //UserAgent.ImmutableUserAgent userAgent = userAgentAnalyzer.parse(request.getHeader("User-Agent"));
        val userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"))
        return userAgent.browser.name + userAgent.version
    }

    /**
     * 获得当天是周几
     */
    @JvmStatic
    val weekDay: String
        get() {
            val weekDays = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            val cal = Calendar.getInstance()
            cal.time = Date()
            var w = cal[Calendar.DAY_OF_WEEK] - 1
            if (w < 0) {
                w = 0
            }
            return weekDays[w]
        }// site-local类型的地址未被发现，先记录候选地址
    // 如果没有发现 non-loopback地址.只能用最次选的方案
// 如果是site-local地址，就是它了// 排除loopback类型地址// 在所有的接口下再遍历IP// 遍历所有的网络接口
    /**
     * 获取当前机器的IP
     *
     * @return /
     */
    @JvmStatic
    val localIp: String
        get() = try {
            var candidateAddress: InetAddress? = null
            // 遍历所有的网络接口
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val anInterface = interfaces.nextElement()
                // 在所有的接口下再遍历IP
                val inetAddresses = anInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddr = inetAddresses.nextElement()
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress) {
                        if (inetAddr.isSiteLocalAddress) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.hostAddress
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.hostAddress
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            val jdkSuppliedAddress = InetAddress.getLocalHost() ?: return ""
            jdkSuppliedAddress.hostAddress
        } catch (e: Exception) {
            ""
        }
}