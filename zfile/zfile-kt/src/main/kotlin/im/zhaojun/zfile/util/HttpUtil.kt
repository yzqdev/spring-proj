package im.zhaojun.zfile.util

import cn.hutool.core.io.FileUtil
import im.zhaojun.zfile.exception.PreviewException
import im.zhaojun.zfile.exception.TextParseException
import im.zhaojun.zfile.model.constant.ZFileConstant
import lombok.extern.slf4j.Slf4j
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.net.URL

/**
 * @author zhaojun
 */
@Slf4j
object HttpUtil {
    /**
     * 获取 URL 对应的文件内容
     *
     * @param   url
     * 文件 URL
     * @return  文件 URL
     */
    @JvmStatic
    fun getTextContent(url: String): String {
        val restTemplate: RestTemplate = SpringContextHolder.Companion.getBean<RestTemplate>("restTemplate")
        val maxFileSize = 1024 * ZFileConstant.TEXT_MAX_FILE_SIZE_KB
        if (getRemoteFileSize(url) > maxFileSize) {
            throw PreviewException("预览文件超出大小, 最大支持 " + FileUtil.readableFileSize(maxFileSize))
        }
        val result: String?
        result = try {
            restTemplate.getForObject(url, String::class.java)
        } catch (e: Exception) {
            throw TextParseException("文件解析异常, 请求 url = " + url + ", 异常信息为 = " + e.message)
        }
        return result ?: ""
    }

    /**
     * 获取远程文件大小
     */
    fun getRemoteFileSize(url: String?): Long {
        var size: Long = 0
        val urlObject: URL
        try {
            urlObject = URL(url)
            val conn = urlObject.openConnection()
            size = conn.contentLength.toLong()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return size
    }
}