package cn.hellohao.service.impl

import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.model.entity.StorageKey
import org.springframework.stereotype.Service
import java.io.File

@Service
class KODOImageupload {
    fun ImageuploadKODO(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
        val returnImage = ReturnImage()
        val key: StorageKey? = null
        val cfg: Configuration
        cfg = if (key!!.endpoint == "1") {
            Configuration(Zone.zone0())
        } else if (key.endpoint == "2") {
            Configuration(Zone.zone1())
        } else if (key.endpoint == "3") {
            Configuration(Zone.zone2())
        } else if (key.endpoint == "4") {
            Configuration(Zone.zoneNa0())
        } else {
            Configuration(Zone.zoneAs0())
        }
        val uploadManager = UploadManager(cfg)
        val auth = Auth.create(key.accessKey, key.accessSecret)
        val upToken = auth.uploadToken(key.bucketName, null, 7200, null)
        var file: File? = null
        try {
            for ((key1, value) in fileMap) {
                val ShortUID = shortUuid
                file = value
                try {
                    val response = uploadManager.put(file, "$username/$ShortUID.$key1", upToken)
                    val putRet = Gson().fromJson(response.bodyString(), DefaultPutRet::class.java)
                    returnImage.imgName = "$username/$ShortUID.$key1"
                    returnImage.imgUrl = key.requestAddress + "/" + username + "/" + ShortUID + "." + key1
                    returnImage.imgSize = value.length()
                    returnImage.code = "200"
                } catch (ex: QiniuException) {
                    val r = ex.response
                    System.err.println(r.toString())
                    try {
                        System.err.println(r.bodyString())
                    } catch (ex2: QiniuException) {
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            returnImage.code = "500"
        }
        return returnImage
    }

    fun delKODO(keyID: String?, fileName: String?): Boolean {
        var b = true
        try {
            KODOImageupload.Companion.bucketManager.delete(KODOImageupload.Companion.key.getBucketName(), fileName)
        } catch (ex: Exception) {
            b = false
        }
        return b
    }

    companion object {
        var upToken: String? = null
        var bucketManager: BucketManager? = null
        var key: StorageKey? = null
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    val cfg: Configuration
                    cfg = if (k.endpoint == "1") {
                        Configuration(Zone.zone0())
                    } else if (k.endpoint == "2") {
                        Configuration(Zone.zone1())
                    } else if (k.endpoint == "3") {
                        Configuration(Zone.zone2())
                    } else if (k.endpoint == "4") {
                        Configuration(Zone.zoneNa0())
                    } else {
                        Configuration(Zone.zoneAs0())
                    }
                    val uploadManager = UploadManager(cfg)
                    val auth = Auth.create(k.accessKey, k.accessSecret)
                    val upToken =
                        auth.uploadToken(k.bucketName, null, 7200, null) //auth.uploadToken(k.getBucketname());
                    var bucketManager = BucketManager(auth, cfg)
                    var fileListIterator: FileListIterator? = null
                    try {
                        fileListIterator = bucketManager.createFileListIterator(k.bucketName, "", 1, "/")
                        val items = fileListIterator.next()
                        if (items != null) {
                            ret = 1
                            bucketManager = bucketManager
                            KODOImageupload.Companion.key = k
                        }
                    } catch (e: Exception) {
                        println("KODO Object Is null")
                        ret = -1
                    }
                }
            }
            return ret
        }
    }
}