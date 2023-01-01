package cn.hellohao.service.impl

import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.util.TypeDict.FileMiME
import org.springframework.stereotype.Service
import java.io.File

@Service
class USSImageupload {
    fun ImageuploadUSS(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
        val returnImage = ReturnImage()
        var file: File? = null
        val meta = ObjectMetadata()
        meta.setHeader("Content-Disposition", "inline")
        try {
            for ((key1, value) in fileMap) {
                val ShortUID = shortUuid
                file = value
                val fileMiME = FileMiME(file)
                meta.setHeader("content-type", fileMiME.data.toString())
                USSImageupload.Companion.upyun.setContentMD5(UpYun.md5(file))
                val result: Boolean = USSImageupload.Companion.upyun.writeFile("$username/$ShortUID.$key1", file, true)
                if (result) {
                    returnImage.imgName = "$username/$ShortUID.$key1"
                    returnImage.imgUrl =
                        USSImageupload.Companion.key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + key1
                    returnImage.imgSize = value.length()
                    returnImage.code = "200"
                } else {
                    returnImage.code = "400"
                    System.err.println("上传失败")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            returnImage.code = "500"
        }
        return returnImage
    }

    fun delUSS(keyID: String?, fileName: String?): Boolean {
        var b = true
        try {
            val result: Boolean = USSImageupload.Companion.upyun.deleteFile(fileName, null)
        } catch (e: Exception) {
            e.printStackTrace()
            b = false
        }
        return b
    }

    companion object {
        var upyun: UpYun? = null
        var key: StorageKey? = null

        //初始化
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            if (k.storageType != null && k.accessKey != null && k.accessSecret != null && k.bucketName != null && k.requestAddress != null) {
                if (k.storageType != "" && k.accessKey != "" && k.accessSecret != "" && k.bucketName != ""
                    && k.requestAddress != ""
                ) {
                    // 初始化
                    // 创建UpYun实例。
                    var upyun = UpYun(k.bucketName, k.accessKey, k.accessSecret)
                    var items: List<FolderItem?>? = null
                    try {
                        items = upyun.readDir("/", null)
                        ret = 1
                        upyun = upyun
                        USSImageupload.Companion.key = k
                    } catch (e: Exception) {
                        println("USS Object Is null")
                        ret = -1
                    }
                }
            }
            return ret
        }
    }
}