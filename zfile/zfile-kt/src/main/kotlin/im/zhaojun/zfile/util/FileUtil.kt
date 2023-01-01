package im.zhaojun.zfile.util

import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import im.zhaojun.zfile.model.constant.LocalFileResponseTypeConstant
import lombok.extern.slf4j.Slf4j
import org.apache.catalina.connector.ClientAbortException
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author zhaojun
 */
@Slf4j
object FileUtil {
    /**
     * 文件下载，单线程，直接传
     * @param file          文件对象
     * @param fileName      要保存为的文件名
     * @return              文件下载对象
     */
    @JvmStatic
    fun exportSingleThread(file: File, fileName: String?): ResponseEntity<Any> {
        var fileName = fileName
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 FILE NOT FOUND")
        }
        val mediaType = MediaType.APPLICATION_OCTET_STREAM
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
        if (StringUtils.isNullOrEmpty(fileName)) {
            fileName = file.name
        }
        headers.setContentDispositionFormData("attachment", URLUtil.encode(fileName))
        headers.add(HttpHeaders.PRAGMA, "no-cache")
        headers.add(HttpHeaders.EXPIRES, "0")
        headers.add(HttpHeaders.LAST_MODIFIED, Date().toString())
        headers.add(HttpHeaders.ETAG, System.currentTimeMillis().toString())
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(mediaType)
            .body(FileSystemResource(file))
    }

    /**
     * 返回文件给 response，支持断点续传和多线程下载
     * @param request       请求对象
     * @param response      响应对象
     * @param file          下载的文件
     */
    fun export(request: HttpServletRequest, response: HttpServletResponse, file: File, type: String?) {
        export(request, response, file, file.name, type)
    }

    /**
     * 返回文件给 response，支持断点续传和多线程下载 (动态变化的文件不支持)
     * @param request       请求对象
     * @param response      响应对象
     * @param file          下载的文件
     * @param fileName      下载的文件名，为空则默认读取文件名称
     */
    fun export(
        request: HttpServletRequest,
        response: HttpServletResponse,
        file: File,
        fileName: String?,
        type: String?
    ) {
        var fileName = fileName
        if (!file.exists()) {
            try {
                response.writer.write("404 FILE NOT FOUND")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (StringUtils.isNullOrEmpty(fileName)) {
            //文件名
            fileName = file.name
        }
        var range = request.getHeader(HttpHeaders.RANGE)
        val rangeSeparator = "-"
        // 开始下载位置
        var startByte: Long = 0
        // 结束下载位置
        var endByte = file.length() - 1

        // 如果是断点续传
        if (range != null && range.contains("bytes=") && range.contains(rangeSeparator)) {
            // 设置响应状态码为 206
            response.status = HttpServletResponse.SC_PARTIAL_CONTENT
            range = range.substring(range.lastIndexOf("=") + 1).trim { it <= ' ' }
            val ranges = range.split(rangeSeparator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                // 判断 range 的类型
                if (ranges.size == 1) {
                    // 类型一：bytes=-2343
                    if (range.startsWith(rangeSeparator)) {
                        endByte = ranges[0].toLong()
                    } else if (range.endsWith(rangeSeparator)) {
                        startByte = ranges[0].toLong()
                    }
                } else if (ranges.size == 2) {
                    startByte = ranges[0].toLong()
                    endByte = ranges[1].toLong()
                }
            } catch (e: NumberFormatException) {
                // 传参不规范，则直接返回所有内容
                startByte = 0
                endByte = file.length() - 1
            }
        } else {
            // 没有 ranges 即全部一次性传输，需要用 200 状态码，这一行应该可以省掉，因为默认返回是 200 状态码
            response.status = HttpServletResponse.SC_OK
        }

        //要下载的长度（endByte 为总长度 -1，这时候要加回去）
        val contentLength = endByte - startByte + 1
        //文件类型
        var contentType = request.servletContext.getMimeType(fileName)
        if (type == LocalFileResponseTypeConstant.DOWNLOAD || StrUtil.isEmpty(contentType)) {
            contentType = "attachment"
        }
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes")
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType)
        // 这里文件名换你想要的，inline 表示浏览器可以直接使用
        // 参考资料：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentType + ";filename=" + URLUtil.encode(fileName))
        response.setHeader(HttpHeaders.CONTENT_LENGTH, contentLength.toString())
        // [要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader(
            HttpHeaders.CONTENT_RANGE,
            "bytes " + startByte + rangeSeparator + endByte + "/" + file.length()
        )
        val outputStream: BufferedOutputStream
        var randomAccessFile: RandomAccessFile? = null
        //已传送数据大小
        var transmitted: Long = 0
        try {
            randomAccessFile = RandomAccessFile(file, "r")
            outputStream = BufferedOutputStream(response.outputStream)
            val buff = ByteArray(4096)
            var len = 0
            randomAccessFile.seek(startByte)
            while (transmitted + len <= contentLength && randomAccessFile.read(buff).also { len = it } != -1) {
                outputStream.write(buff, 0, len)
                transmitted += len.toLong()
                // 本地测试, 防止下载速度过快
                // Thread.sleep(1);
            }
            // 处理不足 buff.length 部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (contentLength - transmitted).toInt())
                outputStream.write(buff, 0, len)
                transmitted += len.toLong()
            }
            outputStream.flush()
            response.flushBuffer()
            randomAccessFile.close()
            // log.trace("下载完毕: {}-{}, 已传输 {}", startByte, endByte, transmitted);
        } catch (e: ClientAbortException) {
            // ignore 用户停止下载
            // log.trace("用户停止下载: {}-{}, 已传输 {}", startByte, endByte, transmitted);
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                randomAccessFile?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}