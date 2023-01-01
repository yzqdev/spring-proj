package cn.hellohao.util

import cn.hellohao.exception.StorageSourceInitException
import cn.hellohao.model.entity.ReturnImage
import cn.hellohao.service.impl.*
import java.io.File

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/7 17:12
 */
object GetSource {
    @kotlin.jvm.JvmStatic
    fun storageSource(type: Int, fileMap: Map<String, File>, userpath: String, keyID: String?): ReturnImage? {
        val nosImageupload: NOSImageupload =
            SpringContextHolder.Companion.getBean<NOSImageupload>(NOSImageupload::class.java)
        val ossImageupload: OSSImageupload =
            SpringContextHolder.Companion.getBean<OSSImageupload>(OSSImageupload::class.java)
        val ussImageupload: USSImageupload =
            SpringContextHolder.Companion.getBean<USSImageupload>(USSImageupload::class.java)
        val kodoImageupload: KODOImageupload =
            SpringContextHolder.Companion.getBean<KODOImageupload>(KODOImageupload::class.java)
        val cosImageupload: COSImageupload =
            SpringContextHolder.Companion.getBean<COSImageupload>(COSImageupload::class.java)
        val ftpImageupload: FTPImageupload =
            SpringContextHolder.Companion.getBean<FTPImageupload>(FTPImageupload::class.java)
        val uFileImageupload: UFileImageupload = SpringContextHolder.Companion.getBean<UFileImageupload>(
            UFileImageupload::class.java
        )
        var returnImage: ReturnImage? = null
        try {
            if (type == 1) {
                returnImage = nosImageupload.Imageupload(fileMap, userpath, keyID)
            } else if (type == 2) {
                returnImage = ossImageupload.ImageuploadOSS(fileMap, userpath, keyID)
            } else if (type == 3) {
                returnImage = ussImageupload.ImageuploadUSS(fileMap, userpath, keyID)
            } else if (type == 4) {
                returnImage = kodoImageupload.ImageuploadKODO(fileMap, userpath, keyID)
            } else if (type == 5) {
                returnImage = LocUpdateImg.ImageuploadLOC(fileMap, userpath, keyID)
            } else if (type == 6) {
                returnImage = cosImageupload.ImageuploadCOS(fileMap, userpath, keyID)
            } else if (type == 7) {
                returnImage = ftpImageupload.ImageuploadFTP(fileMap, userpath, keyID)
            } else if (type == 8) {
                returnImage = uFileImageupload.ImageuploadUFile(fileMap, userpath, keyID)
            } else {
                StorageSourceInitException("GetSource类捕捉异常：未找到存储源")
            }
        } catch (e: Exception) {
            StorageSourceInitException("GetSource类捕捉异常：", e)
            e.printStackTrace()
        }
        return returnImage
    }
}