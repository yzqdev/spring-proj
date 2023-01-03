package org.javaboy.vhr.config

import org.csource.common.MyException
import java.io.IOException

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2020-03-02 23:26
 */
object FastDFSUtils {
    private var client1: StorageClient1? = null

    init {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties")
            val trackerClient = TrackerClient()
            val trackerServer: TrackerServer = org.javaboy.vhr.config.trackerClient.getConnection()
            client1 = StorageClient1(org.javaboy.vhr.config.trackerServer, null)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: MyException) {
            e.printStackTrace()
        }
    }

    fun upload(file: MultipartFile): String? {
        val oldName: String = file.getOriginalFilename()
        try {
            return client1.upload_file1(file.getBytes(), oldName.substring(oldName.lastIndexOf(".") + 1), null)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: MyException) {
            e.printStackTrace()
        }
        return null
    }
}