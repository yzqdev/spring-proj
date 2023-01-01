package cn.hellohao.service.impl

import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.model.entity.StorageKey
import cn.hellohao.util.SetText.shortUuid
import com.aliyun.oss.model.ListObjectsRequest
import com.aliyun.oss.model.ObjectListing
import com.aliyun.oss.model.PutObjectRequest
import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.auth.COSCredentials
import com.qcloud.cos.exception.CosClientException
import com.qcloud.cos.exception.CosServiceException
import org.springframework.stereotype.Service
import java.io.File

@Service
class COSImageupload {
    fun ImageuploadCOS(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
        val returnImage = ReturnImage()
        var file: File? = null
        val ImgUrl: Map<ReturnImage, Int> = HashMap()
        try {
            for ((key1, value) in fileMap) {
                val ShortUID = shortUuid
                file = value
                try {
                    val bucketName = key!!.bucketName
                    val userkey = "$username/$ShortUID.$key1"
                    val putObjectRequest = PutObjectRequest(bucketName, userkey, file)
                    val putObjectResult = cosClient!!.putObject(putObjectRequest)
                    returnImage.imgName = userkey
                    returnImage.imgUrl = key!!.requestAddress + "/" + userkey
                    returnImage.imgSize = value.length()
                    returnImage.code = "200"
                } catch (serverException: CosServiceException) {
                    returnImage.code = "400"
                    serverException.printStackTrace()
                } catch (clientException: CosClientException) {
                    returnImage.code = "400"
                    clientException.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            returnImage.code = "500"
        }
        return returnImage
    }

    fun delCOS(keyID: String?, fileName: String?): Boolean {
        var b = true
        try {
            cosClient!!.deleteObject(key!!.bucketName, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            b = false
        }
        return b
    }

    companion object {
        var cosClient: COSClient? = null
        var key: StorageKey? = null
        @kotlin.jvm.JvmStatic
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    val secretId = k.accessKey
                    val secretKey = k.accessSecret
                    val cred: COSCredentials = BasicCOSCredentials(secretId, secretKey)
                    val region = Region(k.endpoint)
                    val clientConfig = ClientConfig(region)
                    var cosClient = COSClient(cred, clientConfig)
                    val listObjectsRequest = ListObjectsRequest()
                    listObjectsRequest.bucketName = k.bucketName
                    listObjectsRequest.delimiter = "/"
                    listObjectsRequest.maxKeys = 1
                    var objectListing: ObjectListing? = null
                    try {
                        objectListing = cosClient.listObjects(listObjectsRequest)
                        ret = 1
                        cosClient = cosClient
                        key = k
                    } catch (e: Exception) {
                        println("COS Object Is null")
                        ret = -1
                    }
                }
            }
            return ret
        }
    }
}