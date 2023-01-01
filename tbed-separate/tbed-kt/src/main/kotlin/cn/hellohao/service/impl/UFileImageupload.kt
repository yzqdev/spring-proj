package cn.hellohao.service.impl

import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.util.SetText.shortUuid
import cn.hellohao.util.TypeDict.FileMiME
import com.UpYun
import com.aliyun.oss.model.ObjectMetadata
import java.io.File

@Service
class UFileImageupload {
    fun ImageuploadUFile(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
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
                UFileImageupload.Companion.uFile.setContentMD5(UpYun.md5(file))
                val result: Boolean =
                    UFileImageupload.Companion.uFile.writeFile("$username/$ShortUID.$key1", file, true)
                if (result) {
                    returnImage.imgName = "$username/$ShortUID.$key1"
                    returnImage.imgUrl =
                        UFileImageupload.Companion.key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + key1
                    returnImage.imgSize = value.length()
                    returnImage.code = "200"
                } else {
                    returnImage.code = "400"
                    System.err.println("上传失败")
                }
            }
        } catch (e: Exception) {
            returnImage.code = "500"
        }
        return returnImage
    }

    fun delUFile(keyID: String?, fileName: String?): Boolean {
        var b = true
        try {
            val result: Boolean = UFileImageupload.Companion.uFile.deleteFile(fileName, null)
        } catch (e: Exception) {
            e.printStackTrace()
            b = false
        }
        return b
    }

    companion object {
        var uFile: UpYun? = null
        var key: StorageKey? = null

        //ufile初始化
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            if (k.accessSecret != null && k.accessKey != null && k.bucketName != null && k.requestAddress != null) {
                if (k.accessSecret != "" && k.accessKey != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    // 创建UpYun实例。
                    var uFile = UpYun(k.bucketName, k.accessKey, k.accessSecret)
                    var items: List<FolderItem?>? = null
                    try {
                        items = uFile.readDir("/", null)
                        ret = 1
                        uFile = uFile
                        UFileImageupload.Companion.key = k
                    } catch (e: Exception) {
                        println("UFile Object Is null")
                        ret = -1
                    }
                }
            }
            return ret
        }
    }
}