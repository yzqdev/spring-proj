package cn.hellohao.service.impl

@Service
class FTPImageupload {
    fun ImageuploadFTP(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
        val returnImage = ReturnImage()
        val key: StorageKey? = null
        val host = key!!.endpoint.split("\\:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val h = host[0]
        val p = host[1].toInt()
        val ftps = FTPUtils(h, p, key.accessKey, key.accessSecret)
        val flag = ftps.open()
        var file: File? = null
        val ImgUrl: Map<ReturnImage, Int> = HashMap()
        ftps.mkDir(File.separator + username)
        try {
            for ((key1, value) in fileMap) {
                val ShortUID = shortUuid
                file = value
                val userkey = "$username/$ShortUID.$key1"
                if (flag) {
                    val isUpload = ftps.upload(file, "/$userkey", "")
                    if (isUpload) {
                        returnImage.imgName = userkey
                        returnImage.imgUrl = key.requestAddress + "/" + userkey
                        returnImage.imgSize = value.length()
                        returnImage.code = "200"
                    } else {
                        returnImage.code = "500"
                    }
                }
                Print.Normal("要上传的文件路径：/$userkey")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            returnImage.code = "500"
            //                ImgUrl.put(null, 500);
        }
        return returnImage
    }

    fun delFTP(keyID: String?, fileName: String?): Boolean {
        var b = true
        b = try {
            val host = key!!.endpoint.split("\\:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val h = host[0]
            val p = host[1].toInt()
            //创建FTP客户端，所有的操作都基于FTPClinet
            val ftps = FTPUtils(h, p, key!!.accessKey, key!!.accessSecret)
            ftps.open()
            ftps.deleteFile(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return b
    }

    companion object {
        var ftpClient1: FTPClient? = null
        var key: StorageKey? = null

        //初始化FTP对象存储
        @kotlin.jvm.JvmStatic
        fun Initialize(k: StorageKey): Int {
            var ret = -1
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != "" && k.requestAddress != "") {
                    val ftp = FTPClient()
                    ftp.connectTimeout = 0
                    val flag = k.endpoint.indexOf(":")
                    if (flag > 0) {
                        val host = k.endpoint.split("\\:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val h = host[0]
                        val p = host[1].toInt()
                        try {
                            if (!ftp.isConnected) {
                                ftp.connect(h, p)
                            }
                            ftp.login(k.accessKey, k.accessSecret)
                            val reply = ftp.replyCode
                            if (!FTPReply.isPositiveCompletion(reply)) {
                                println("FTP Object Is null")
                                ftp.disconnect()
                                return -1
                            }
                            ret = 1
                            key = k
                            //                        KeyObjectMap objectMap = new KeyObjectMap(ftp,k);
                        } catch (e: IOException) {
                            println("FTP Object Is null")
                            return -1
                        }
                    }
                }
            }
            return ret
        }

        fun fuzhi(p1: String, p2: String?) {
            try {
                val afile = File(p1)
                val bfile = File(p2) //定义一个复制后的文件路径
                bfile.createNewFile() //新建文件
                val c = FileInputStream(afile) //定义FileInputStream对象
                val d = FileOutputStream(bfile) //定义FileOutputStream对象
                val date = ByteArray(512) //定义一个byte数组
                var i = 0
                while (c.read(date).also { i = it } > 0) { //判断有没有读取到文件末尾
                    d.write(date)
                }
                c.close()
                d.close()
                println("文件复制成功")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}