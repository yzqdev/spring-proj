package cn.hellohao.auth.filter.xss

import com.alibaba.fastjson.JSON
import org.apache.commons.text.StringEscapeUtils
import java.io.*
import java.nio.charset.Charset
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * ServletRequest包装类,对request做XSS过滤处理
 * @author Hellohao
 */
class XssHttpServletRequestWrapper(request: HttpServletRequest?) : HttpServletRequestWrapper(request) {
    override fun getHeader(name: String): String {
        return StringEscapeUtils.escapeHtml4(super.getHeader(name))
    }

    override fun getQueryString(): String {
        return StringEscapeUtils.escapeHtml4(super.getQueryString())
    }

    override fun getParameter(name: String): String {
        return StringEscapeUtils.escapeHtml4(super.getParameter(name))
    }

    override fun getParameterValues(name: String): Array<String> {
        val values = super.getParameterValues(name)
        if (values != null) {
            val length = values.size
            val escapseValues = arrayOfNulls<String>(length)
            for (i in 0 until length) {
                escapseValues[i] = StringEscapeUtils.escapeHtml4(values[i])
            }
            return escapseValues
        }
        return values
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        var str = getRequestBody(super.getInputStream())
        val map: Map<String, Any> = JSON.parseObject<Map<*, *>>(str, MutableMap::class.java)
        val resultMap: MutableMap<String, Any?> = HashMap(map.size)
        for (key in map.keys) {
            val `val` = map[key]
            if (map[key] is String) {
                resultMap[key] = StringEscapeUtils.escapeHtml4(`val`.toString())
            } else {
                resultMap[key] = `val`
            }
        }
        str = JSON.toJSONString(resultMap)
        val bais = ByteArrayInputStream(str.toByteArray())
        return object : ServletInputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return bais.read()
            }

            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(listener: ReadListener) {}
        }
    }

    private fun getRequestBody(stream: InputStream): String {
        var line: String? = ""
        val body = StringBuilder()
        var counter = 0

        // 读取POST提交的数据内容
        val reader = BufferedReader(InputStreamReader(stream, Charset.forName("UTF-8")))
        try {
            while (reader.readLine().also { line = it } != null) {
                body.append(line)
                counter++
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return body.toString()
    }
}