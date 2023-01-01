package cn.hellohao.util

import cn.hellohao.model.entity.Msg
import org.apache.tika.Tika
import java.awt.Image
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object TypeDict {
    /**
     * 常用文件的文件头如下：(以前六位为准)
     * JPEG (jpg)，文件头：FFD8FF
     * PNG (png)，文件头：89504E47
     * GIF (gif)，文件头：47494638
     * TIFF (tif)，文件头：49492A00
     * Windows Bitmap (bmp)，文件头：424D
     * CAD (dwg)，文件头：41433130
     * Adobe Photoshop (psd)，文件头：38425053
     * Rich Text Format (rtf)，文件头：7B5C727466
     * XML (xml)，文件头：3C3F786D6C
     * HTML (html)，文件头：68746D6C3E
     * Email [thorough only] (eml)，文件头：44656C69766572792D646174653A
     * Outlook Express (dbx)，文件头：CFAD12FEC5FD746F
     * Outlook (pst)，文件头：2142444E
     * MS Word/Excel (xls.or.doc)，文件头：D0CF11E0
     * MS Access (mdb)，文件头：5374616E64617264204A
     * WordPerfect (wpd)，文件头：FF575043
     * Postscript (eps.or.ps)，文件头：252150532D41646F6265
     * Adobe Acrobat (pdf)，文件头：255044462D312E
     * Quicken (qdf)，文件头：AC9EBD8F
     * Windows Password (pwl)，文件头：E3828596
     * ZIP Archive (zip)，文件头：504B0304
     * RAR Archive (rar)，文件头：52617221
     * Wave (wav)，文件头：57415645
     * AVI (avi)，文件头：41564920
     * Real Audio (ram)，文件头：2E7261FD
     * Real Media (rm)，文件头：2E524D46
     * MPEG (mpg)，文件头：000001BA
     * MPEG (mpg)，文件头：000001B3
     * Quicktime (mov)，文件头：6D6F6F76
     * Windows Media (asf)，文件头：3026B2758E66CF11
     * MIDI (mid)，文件头：4D546864
     */
    fun checkType(xxxx: String?): String {
        return when (xxxx) {
            "FFD8FF" -> "jpg"
            "89504E" -> "png"
            "474946" -> "jif"
            "47494638" -> "gif"
            "424D" -> "bmp"
            "424D36" -> "bmp"
            else -> "0000"
        }
    }

    fun checkImgType(file: File?): Boolean {
        return try {
            // 通过ImageReader来解码这个file并返回一个BufferedImage对象
            // 如果找不到合适的ImageReader则会返回null，我们可以认为这不是图片文件
            // 或者在解析过程中报错，也返回false
            val image: Image? = ImageIO.read(file)
            image != null
        } catch (ex: IOException) {
            false
        }
    }

    //apache大法
    @kotlin.jvm.JvmStatic
    fun FileMiME(file: File?): Msg {
        val msg = Msg()
        try {
            val tika = Tika()
            val fileType = tika.detect(file)
            if (fileType != null && fileType.contains("/")) {
                if (fileType.contains("image/")) {
                    msg.data = fileType
                } else {
                    //非图像类型
                    msg.code = "110602"
                    msg.info = "该文件非图像文件，或不受支持"
                }
            }
        } catch (e: Exception) {
            System.err.println("这是一个图像类别鉴定的报错:161")
            msg.code = "110603" //图像格式不受支持
            msg.info = "暂时不能上传该文件"
        }
        return msg
    }
}