package cn.hellohao.util

import cn.hellohao.config.GlobalConstant
import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.service.impl.KeysServiceImpl
import java.io.*

object LocUpdateImg {
    @kotlin.jvm.JvmStatic
    fun deleteLOCImg(imagename: String): Boolean {
        var isDele = false
        isDele = try {
            val filePath = GlobalConstant.LOCPATH + File.separator + imagename
            val file = File(filePath)
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return isDele
    }

    @Throws(Exception::class)
    fun ImageuploadLOC(fileMap: Map<String, File>, username: String, keyID: String?): ReturnImage {
        val keysService: KeysServiceImpl =
            SpringContextHolder.Companion.getBean<KeysServiceImpl>(KeysServiceImpl::class.java)
        val key = keysService.selectKeys(keyID)
        val returnImage = ReturnImage()
        val filePath = GlobalConstant.LOCPATH + File.separator
        val file: File? = null
        for ((key1, value) in fileMap) {
            val ShortUID = SetText.getShortUuid()
            println("待上传的图片：" + username + File.separator + ShortUID + "." + key1)
            val dest = File(filePath + username + File.separator + ShortUID + "." + key1)
            val temppath = File(filePath + username + File.separator)
            if (!dest.parentFile.exists()) {
                dest.parentFile.mkdirs()
            }
            try {
                val fileInputStream: InputStream = FileInputStream(value)
                val bos = BufferedOutputStream(FileOutputStream(dest))
                val bs = ByteArray(1024)
                var len: Int
                while (fileInputStream.read(bs).also { len = it } != -1) {
                    bos.write(bs, 0, len)
                }
                bos.flush()
                bos.close()
                returnImage.imgName = "$username/$ShortUID.$key1" //entry.getValue().getOriginalFilename()
                returnImage.imgUrl = key.requestAddress + "/ota/" + username + "/" + ShortUID + "." + key1
                returnImage.imgSize = value.length()
                returnImage.code = "200"
            } catch (e: IOException) {
                e.printStackTrace()
                returnImage.code = "500"
                System.err.println("上传失败")
            }
        }
        return returnImage
    }
}