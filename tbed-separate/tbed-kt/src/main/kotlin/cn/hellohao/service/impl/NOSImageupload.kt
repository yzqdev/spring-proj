package cn.hellohao.service.impl

@Service
class NOSImageupload {
    fun Imageupload(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
        val returnImage = ReturnImage()
        var file: File? = null
        try {
            for ((key1, value) in fileMap) {
                val ShortUID = shortUuid
                file = value
                NOSImageupload.Companion.nosClient.putObject(
                    NOSImageupload.Companion.key.getBucketName(),
                    "$username/$ShortUID.$key1",
                    file
                )
                returnImage.imgName = "$username/$ShortUID.$key1"
                returnImage.imgUrl =
                    NOSImageupload.Companion.key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + key1
                returnImage.imgSize = value.length()
                returnImage.code = "200"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            returnImage.code = "500"
        }
        return returnImage
    }

    fun delNOS(keyID: String?, fileName: String?): Boolean {
        var b = true
        try {
            //这种方法不能删除指定文件夹下的文件
            val isExist: Boolean = NOSImageupload.Companion.nosClient.doesObjectExist(
                NOSImageupload.Companion.key.getBucketName(),
                fileName,
                null
            )
            if (isExist) {
                NOSImageupload.Companion.nosClient.deleteObject(NOSImageupload.Companion.key.getBucketName(), fileName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            b = false
        }
        return b
    }

    companion object {
        var nosClient: NosClient? = null
        var key: StorageKey? = null

        //初始化网易NOS对象存储
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    // 初始化
                    val credentials: Credentials = BasicCredentials(k.accessKey, k.accessSecret)
                    var nosClient = NosClient(credentials)
                    nosClient.setEndpoint(k.endpoint)
                    var objectListing: ObjectListing? = null
                    try {
                        objectListing = nosClient.listObjects(k.bucketName)
                        ret = 1
                        nosClient = nosClient
                        NOSImageupload.Companion.key = k
                    } catch (e: Exception) {
                        println("NOS Object Is null")
                        ret = -1
                    }
                }
            }
            //throw new StorageSourceInitException("当前数据源配置不完整，请管理员前往后台配置。");
            return ret
        }
    }
}