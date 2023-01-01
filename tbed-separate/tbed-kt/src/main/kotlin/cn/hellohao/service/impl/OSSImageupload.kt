package cn.hellohao.service.impl

import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.model.entity.StorageKey
import cn.hellohao.util.SetText.shortUuid
import cn.hellohao.util.TypeDict.FileMiME
import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.ObjectListing
import com.aliyun.oss.model.ObjectMetadata
import org.springframework.stereotype.Service
import java.io.File

@Service
class OSSImageupload {
    fun ImageuploadOSS(fileMap: Map<String, File?>, username: String, keyID: String?): ReturnImage {
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
                println("待上传的图片：$username/$ShortUID.$key1")
                OSSImageupload.Companion.ossClient.putObject(
                    OSSImageupload.Companion.key.getBucketName(),
                    "$username/$ShortUID.$key1",
                    file
                )
                returnImage.imgName = "$username/$ShortUID.$key1" //entry.getValue().getOriginalFilename()
                returnImage.imgUrl =
                    OSSImageupload.Companion.key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + key1
                returnImage.imgSize = file!!.length()
                returnImage.code = "200"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            returnImage.code = "500"
        }
        return returnImage
    }

    fun delOSS(keyID: String?, fileName: String?): Boolean {
        var b = true
        try {
            OSSImageupload.Companion.ossClient.deleteObject(OSSImageupload.Companion.key.getBucketName(), fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            b = false
        }
        return b
    }

    companion object {
        var ossClient: OSSClient? = null
        var key: StorageKey? = null
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            var objectListing: ObjectListing? = null
            if (k.endpoint != null && k.accessSecret != null && k.accessKey != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.accessKey != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    var ossClient = OSSClientBuilder().build(k.endpoint, k.accessKey, k.accessSecret)
                    try {
                        objectListing = ossClient.listObjects(k.bucketName)
                        ret = 1
                        ossClient = ossClient
                        OSSImageupload.Companion.key = k
                    } catch (e: Exception) {
                        println("OSS Object Is null")
                        ret = -1
                    }
                }
            }
            return ret
        }
    }
}