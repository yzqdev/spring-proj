package com.kuang.utils

import com.kuang.model.entity.User
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author: dobell
 * @link: http://www.dobell.me/
 * @description:
 */
object RequestHelper {
    const val REQUEST_HEADER_USER_AGENT = "user-agent"
    const val REQUEST_HEADER_REFERER = "referer"
    const val protocol = "Sec-WebSocket-Protocol"
    fun getWebSocketProtocol(httpServletRequest: HttpServletRequest): String {
        return httpServletRequest.getHeader(protocol)
    }

    val request: HttpServletRequest
        get() = (Objects.requireNonNull(RequestContextHolder.getRequestAttributes()) as ServletRequestAttributes).request

    /**
     * 获取session中的用户,添加全局对象
     *
     * @return
     */
    @JvmStatic
    val sessionUser: User
        get() {
            val requestAttributes = RequestContextHolder.getRequestAttributes()
            return requestAttributes.getAttribute("loginUser", RequestAttributes.SCOPE_REQUEST) as User
        }

    /**
     * 判断请求是否为Ajax请求.
     *
     * @param request
     * @return
     */
    fun isAjaxRequest(request: HttpServletRequest): Boolean {
        return "XMLHttpRequest" == request.getHeader("X-Requested-With")
    }

    // 多次反向代理后会有多个IP值，第一个为 真实 ip
    val requestIp: String
        get() {
            val request = request
            var ip = request.getHeader("X-Real-IP")
            if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip, ignoreCase = true)) {
                return ip.trim { it <= ' ' }
            }
            ip = request.getHeader("X-Forwarded-For")
            if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip, ignoreCase = true)) {
                // 多次反向代理后会有多个IP值，第一个为 真实 ip
                return StringUtils.split(ip, ",")[0].trim { it <= ' ' }
            }
            ip = request.getHeader("Proxy-Client-IP")
            if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip, ignoreCase = true)) {
                return ip.trim { it <= ' ' }
            }
            ip = request.getHeader("WL-Proxy-Client-IP")
            if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip, ignoreCase = true)) {
                return ip.trim { it <= ' ' }
            }
            ip = request.getHeader("HTTP_CLIENT_IP")
            if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip, ignoreCase = true)) {
                return ip.trim { it <= ' ' }
            }
            ip = request.getHeader("X-Cluster-Client-IP")
            return if (StringUtils.isNotBlank(ip) && !"unknown".equals(ip, ignoreCase = true)) {
                ip.trim { it <= ' ' }
            } else request.remoteAddr
        }

    fun isApplicationJsonHeader(request: HttpServletRequest): Boolean {
        val contentType = request.getHeader(HttpHeaders.CONTENT_TYPE)
        return contentType != null && StringUtils.replaceAll(
            contentType.trim { it <= ' ' },
            StringUtils.SPACE,
            StringUtils.EMPTY
        ).contains(MediaType.APPLICATION_JSON_VALUE)
    }

    fun getRequestHeader(headerName: String?): String {
        return request.getHeader(headerName)
    }

    val userAgentHeader: String
        get() = getRequestHeader(REQUEST_HEADER_USER_AGENT)

    @Throws(IOException::class)
    fun getRequestMessage(request: HttpServletRequest): String {
        val parameters = StringBuilder()
        return getRequestMessage(request, parameters)
    }

    @Throws(IOException::class)
    private fun getRequestMessage(request: HttpServletRequest, parameters: StringBuilder): String {
        parameters.append("\n请求URL : ")
            .append(request.requestURI)
            .append("\n请求URI : ")
            .append(request.requestURL)
            .append("\n请求方式 : ")
            .append(request.method)
            .append(if (isAjaxRequest(request)) "\tajax请求" else "\t同步请求")
            .append("\n请求者IP : ")
            .append(request.remoteAddr)
            .append("\nSESSION_ID : ")
            .append(request.session.id)
            .append("\n请求时间 : ")
            .append(Instant.now())
        // 请求头
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val element = headerNames.nextElement()
            if (null != element) {
                val header = request.getHeader(element)
                parameters.append("\n请求头内容 : ").append(element).append("=").append(header)
            }
        }
        parameters.append("\n请求参数 : ").append(getRequestBody(request))
        // 请求Session内容
        val sessionAttributeNames = request.session.attributeNames
        while (sessionAttributeNames.hasMoreElements()) {
            parameters.append("\nSession内容 : ").append(sessionAttributeNames.nextElement())
        }
        return parameters.toString()
    }

    val httpServletRequest: HttpServletRequest
        get() = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

    /**
     * 得到请求的根目录
     *
     * @return
     */
    val basePath: String
        get() {
            val request = httpServletRequest
            val path = contextPath
            return (request.scheme + "://" + request.serverName
                    + ":" + request.serverPort + path)
        }

    /**
     * 得到结构目录
     *
     * @return
     */
    val contextPath: String
        get() {
            val request = httpServletRequest
            return request.contextPath
        }

    @Throws(IOException::class)
    fun getRequestBody(request: HttpServletRequest): String {
        val inputStream = request.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        var s: String?
        val sb = StringBuilder()
        while (reader.readLine().also { s = it } != null) {
            sb.append(s)
        }
        reader.close()
        return sb.toString()
    }

    @get:Throws(IOException::class)
    val requestBody: String
        get() {
            val request = httpServletRequest
            val inputStream = request.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var s: String?
            val sb = StringBuilder()
            while (reader.readLine().also { s = it } != null) {
                sb.append(s)
            }
            reader.close()
            return sb.toString()
        }

    fun getRequestHeaders(type: String): Map<String,Any> {
        val heads: MutableMap<String,Any> = HashMap<String,Any>()
        val list: MutableList<String> = ArrayList()
        list.add(type)
        heads["type"] = list
        return heads
    }
}