package cn.hellohao.util

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.text.DecimalFormat

object SetFiles {
    // 转换文件方法
    fun changeFile(multipartFile: MultipartFile): File? {
        // 获取文件名
        val fileName = multipartFile.name //multipartFile.getOriginalFilename();
        // 获取文件后缀
        val prefix = fileName.substring(fileName.lastIndexOf("."))
        // todo 修改临时文件文件名
        var file: File? = null
        try {
            file = File.createTempFile(fileName, prefix)
            multipartFile.transferTo(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun changeFile_c(multipartFile: MultipartFile): File? {
        // 获取文件名
        val fileName = multipartFile.originalFilename //multipartFile.getOriginalFilename();
        // 获取文件后缀
        val prefix = fileName.substring(fileName.lastIndexOf("."))
        // todo 修改临时文件文件名
        var file: File? = null
        try {
            file = File.createTempFile(fileName, prefix)
            multipartFile.transferTo(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    // 转换文件方法
    @kotlin.jvm.JvmStatic
    fun changeFile_new(multipartFile: MultipartFile): File? {
        // 获取文件名
        val fileName = multipartFile.originalFilename //getOriginalFilename
        // 获取文件后缀
        val prefix = fileName.substring(fileName.lastIndexOf("."))
        // todo 修改临时文件文件名
        var file: File? = null
        try {
            file = File.createTempFile(fileName, prefix)
            multipartFile.transferTo(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    //文件大小单位转换
    @kotlin.jvm.JvmStatic
    fun readableFileSize(fileS: Long): String {
        if (fileS == 0L) {
            return "0B"
        }
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        fileSizeString = if (fileS < 1024) {
            df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            if (fileS < 1099511627776L) {
                df.format(fileS.toDouble() / 1073741824) + "GB"
            } else {
                df.format(fileS.toDouble() / 1099511627776L) + "TB"
            }
        }
        return fileSizeString
    }
}