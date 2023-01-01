package cn.hellohao.util

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/*
操作网络url图片工具类
* */
object ImgUrlUtil {
    fun bytesToHexString(src: ByteArray?): String? {
        val stringBuilder = StringBuilder()
        if (src == null || src.size <= 0) {
            return null
        }
        for (i in src.indices) {
            val v = src[i].toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }

    /**
     * 获取网络文件大小
     */
    @kotlin.jvm.JvmStatic
    @Throws(IOException::class)
    fun getFileLength(downloadUrl: String?): Long {
        if (downloadUrl == null || "" == downloadUrl || downloadUrl.length <= 7) {
            return 0L
        }
        val url = URL(downloadUrl)
        var conn: HttpURLConnection? = null
        return try {
            conn = url.openConnection() as HttpURLConnection
            conn!!.requestMethod = "HEAD"
            conn.setRequestProperty(
                "SysUser-Agent",
                "Mozilla/5.0 (Windows 7; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36 YNoteCef/5.8.0.1 (Windows)"
            )
            conn.contentLength.toLong()
        } catch (e: IOException) {
            0L
        } finally {
            conn!!.disconnect()
        }
    }

    /*
     * 检测Url的响应值
     * 判断是否能访问。
     * */
    fun checkURLStatusCode(urlStr: String?): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        try {
            val url = URL(urlStr)
            val rulConnection = url.openConnection()
            val httpUrlConnection = rulConnection as HttpURLConnection
            httpUrlConnection.connectTimeout = 300000
            httpUrlConnection.readTimeout = 300000
            httpUrlConnection.connect()
            val code = httpUrlConnection.responseCode.toString()
            val message = httpUrlConnection.responseMessage
            println("getResponseCode code =$code")
            println("getResponseMessage message =$message")
            if (!code.startsWith("2") && !code.startsWith("3")) {
                map["Check"] = "false"
                map["StatusCode"] = code
                throw Exception("ResponseCode is not begin with 2,code=$code")
            }
            map["Check"] = "true"
            println("连接正常")
        } catch (ex: Exception) {
            println(ex.message)
        }
        return map
    }

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    @kotlin.jvm.JvmStatic
    fun downLoadFromUrl(urlStr: String?, fileName: String, savePath: String?): Map<String, Any?> {
        val resmap: MutableMap<String, Any?> = HashMap()
        val map = checkURLStatusCode(urlStr)
        if (map.size == 0 || map["Check"] == "false") {
//            StatusCode
            resmap["res"] = false
            resmap["StatusCode"] = map["StatusCode"]
            return resmap
        }
        return try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            //设置超时间为3秒
            conn.connectTimeout = 5 * 1000
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("SysUser-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")

            //得到输入流
            val inputStream = conn.inputStream
            //获取自己数组
            val getData = readInputStream(inputStream)
            val saveDir = File(savePath)
            if (!saveDir.exists()) {
                saveDir.mkdir()
            }
            val file = File(saveDir.toString() + File.separator + fileName)
            val fos = FileOutputStream(file)
            fos.write(getData)
            if (fos != null) {
                fos.close()
            }
            inputStream?.close()
            //下载并且保存成功后 判断格式 如果不是图像格式 就删除
//
            if (File(saveDir.toString() + File.separator + fileName).exists()) {
                val msg = TypeDict.FileMiME(file)
                if (msg!!.code == "200") {
                    val f = File(saveDir.toString() + File.separator + fileName)
                    val imgPath =
                        saveDir.toString() + File.separator + fileName + "." + msg.data.toString().replace("image/", "")
                    f.renameTo(File(imgPath))
                    resmap["res"] = true
                    resmap["imgPath"] = imgPath
                    resmap["imgsize"] = File(imgPath).length()
                } else {
                    File(saveDir.toString() + File.separator + fileName).delete()
                    resmap["res"] = false
                    resmap["StatusCode"] = "110403"
                }
            } else {
                resmap["res"] = false
                resmap["StatusCode"] = "500"
            }
            resmap
        } catch (e: Exception) {
            e.printStackTrace()
            resmap["res"] = false
            resmap["StatusCode"] = "500"
            resmap
        }
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws
     */
    @Throws(IOException::class)
    fun readInputStream(inputStream: InputStream?): ByteArray {
        val buffer = ByteArray(1024)
        var len = 0
        val bos = ByteArrayOutputStream()
        while (inputStream!!.read(buffer).also { len = it } != -1) {
            bos.write(buffer, 0, len)
        }
        bos.close()
        return bos.toByteArray()
    }

    /**
     * 根据java.io.*的流获取文件大小
     * @param file
     */
    fun getFileSize2(file: File): Int {
        var imgsize = 0
        var fis: FileInputStream? = null
        try {
            if (file.exists() && file.isFile) {
                val fileName = file.name
                fis = FileInputStream(file)
                imgsize = fis.available() / 1024
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != fis) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return imgsize
    }
}