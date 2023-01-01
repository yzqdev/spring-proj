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

import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.poi.excel.ExcelUtil
import me.zhengjie.exception.BadRequestException
import org.apache.poi.util.IOUtils
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.security.MessageDigest
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * File工具类，扩展 hutool 工具包
 *
 * @author Zheng Jie
 * @date 2018-12-27
 */
object FileUtil : cn.hutool.core.io.FileUtil() {
    private val log = LoggerFactory.getLogger(FileUtil::class.java)

    /**
     * 系统临时目录
     * <br></br>
     * windows 包含路径分割符，但Linux 不包含,
     * 在windows \\==\ 前提下，
     * 为安全起见 同意拼装 路径分割符，
     * <pre>
     * java.io.tmpdir
     * windows : C:\Users/xxx\AppData\Local\Temp\
     * linux: /temp
    </pre> *
     */
    @JvmField
    val SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator

    /**
     * 定义GB的计算常量
     */
    private const val GB = 1024 * 1024 * 1024

    /**
     * 定义MB的计算常量
     */
    private const val MB = 1024 * 1024

    /**
     * 定义KB的计算常量
     */
    private const val KB = 1024

    /**
     * 格式化小数
     */
    private val DF = DecimalFormat("0.00")
    const val IMAGE = "图片"
    const val TXT = "文档"
    const val MUSIC = "音乐"
    const val VIDEO = "视频"
    const val OTHER = "其他"

    /**
     * MultipartFile转File
     */
    @JvmStatic
    fun toFile(multipartFile: MultipartFile): File? {
        // 获取文件名
        val fileName = multipartFile.originalFilename
        // 获取文件后缀
        val prefix = "." + getExtensionName(fileName)
        var file: File? = null
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File(SYS_TEM_DIR + IdUtil.simpleUUID() + prefix)
            // MultipartFile to File
            multipartFile.transferTo(file)
        } catch (e: IOException) {
            log.error(e.message, e)
        }
        return file
    }

    /**
     * 获取文件扩展名，不带 .
     */
    @JvmStatic
    fun getExtensionName(filename: String?): String? {
        if (filename != null && filename.length > 0) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length - 1) {
                return filename.substring(dot + 1)
            }
        }
        return filename
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    @JvmStatic
    fun getFileNameNoEx(filename: String?): String? {
        if (filename != null && filename.length > 0) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length) {
                return filename.substring(0, dot)
            }
        }
        return filename
    }

    /**
     * 文件大小转换
     */
    @JvmStatic
    fun getSize(size: Long): String {
        val resultSize: String
        resultSize = if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            DF.format((size / GB.toFloat()).toDouble()) + "GB   "
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            DF.format((size / MB.toFloat()).toDouble()) + "MB   "
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            DF.format((size / KB.toFloat()).toDouble()) + "KB   "
        } else {
            size.toString() + "B   "
        }
        return resultSize
    }

    /**
     * inputStream 转 File
     */
    @JvmStatic
    fun inputStreamToFile(ins: InputStream, name: String): File {
        val file = File(SYS_TEM_DIR + name)
        if (file.exists()) {
            return file
        }
        var os: OutputStream? = null
        try {
            os = FileOutputStream(file)
            var bytesRead: Int
            val len = 8192
            val buffer = ByteArray(len)
            while (ins.read(buffer, 0, len).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtil.close(os)
            CloseUtil.close(ins)
        }
        return file
    }

    /**
     * 将文件名解析成文件的上传路径
     */
    @JvmStatic
    fun upload(file: MultipartFile, filePath: String): File? {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmssS")
        val name = getFileNameNoEx(file.originalFilename)
        val suffix = getExtensionName(file.originalFilename)
        val nowStr = "-" + dateTimeFormatter.format(LocalDateTime.now())
        try {
            val fileName = "$name$nowStr.$suffix"
            val path = filePath + fileName
            // getCanonicalFile 可解析正确各种路径
            val dest = File(path).canonicalFile
            // 检测是否存在目录
            if (!dest.parentFile.exists()) {
                if (!dest.parentFile.mkdirs()) {
                    println("was not successful.")
                }
            }
            // 文件写入
            file.transferTo(dest)
            return dest
        } catch (e: Exception) {
            log.error(e.message, e)
        }
        return null
    }

    /**
     * 导出excel
     */
    @JvmStatic
    @Throws(IOException::class)
    fun downloadExcel(list: List<Map<String?, Any?>?>?, response: HttpServletResponse) {
        val tempPath = SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx"
        val file = File(tempPath)
        val writer = ExcelUtil.getBigWriter(file)
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true)
        val sheet = writer.sheet as SXSSFSheet
        //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
        sheet.trackAllColumnsForAutoSizing()
        //列宽自适应
        writer.autoSizeColumnAll()
        //response为HttpServletResponse对象
        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx")
        val out = response.outputStream
        // 终止后删除临时文件
        file.deleteOnExit()
        writer.flush(out, true)
        //此处记得关闭输出Servlet流
        IoUtil.close(out)
    }

    fun getFileType(type: String?): String {
        val documents = "txt doc pdf ppt pps xlsx xls docx"
        val music = "mp3 wav wma mpa ram ra aac aif m4a"
        val video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg"
        val image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg"
        return if (image.contains(type!!)) {
            IMAGE
        } else if (documents.contains(type)) {
            TXT
        } else if (music.contains(type)) {
            MUSIC
        } else if (video.contains(type)) {
            VIDEO
        } else {
            OTHER
        }
    }

    @JvmStatic
    fun checkSize(maxSize: Long, size: Long) {
        // 1M
        val len = 1024 * 1024
        if (size > maxSize * len) {
            throw BadRequestException("文件超出规定大小")
        }
    }

    /**
     * 判断两个文件是否相同
     */
    fun check(file1: File, file2: File): Boolean {
        val img1Md5 = getMd5(file1)
        val img2Md5 = getMd5(file2)
        return if (img1Md5 != null) {
            img1Md5 == img2Md5
        } else false
    }

    /**
     * 判断两个文件是否相同
     */
    fun check(file1Md5: String, file2Md5: String): Boolean {
        return file1Md5 == file2Md5
    }

    private fun getByte(file: File): ByteArray? {
        // 得到文件长度
        val b = ByteArray(file.length().toInt())
        var `in`: InputStream? = null
        try {
            `in` = FileInputStream(file)
            try {
                println(`in`.read(b))
            } catch (e: IOException) {
                log.error(e.message, e)
            }
        } catch (e: Exception) {
            log.error(e.message, e)
            return null
        } finally {
            CloseUtil.close(`in`)
        }
        return b
    }

    private fun getMd5(bytes: ByteArray?): String? {
        // 16进制字符
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        try {
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(bytes)
            val md = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            // 移位 输出字符串
            for (byte0 in md) {
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            return String(str)
        } catch (e: Exception) {
            log.error(e.message, e)
        }
        return null
    }

    /**
     * 下载文件
     *
     * @param request  /
     * @param response /
     * @param file     /
     */
    @JvmStatic
    fun downloadFile(request: HttpServletRequest, response: HttpServletResponse, file: File, deleteOnExit: Boolean) {
        response.characterEncoding = request.characterEncoding
        response.contentType = "application/octet-stream"
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            response.setHeader("Content-Disposition", "attachment; filename=" + file.name)
            IOUtils.copy(fis, response.outputStream)
            response.flushBuffer()
        } catch (e: Exception) {
            log.error(e.message, e)
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                    if (deleteOnExit) {
                        file.deleteOnExit()
                    }
                } catch (e: IOException) {
                    log.error(e.message, e)
                }
            }
        }
    }

    fun getMd5(file: File): String? {
        return getMd5(getByte(file))
    }
}