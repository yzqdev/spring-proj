package cn.hellohao.service.impl

import cn.hellohao.service.*
import cn.hellohao.mapper.*
import cn.hellohao.model.entity.*
import cn.hellohao.util.Base64Encryption
import cn.hellohao.util.GetIPS.Companion.getIpAddr
import cn.hellohao.util.GetSource.storageSource
import cn.hellohao.util.SetFiles.changeFile_new
import cn.hellohao.util.SetText.getSubString
import cn.hellohao.util.TypeDict.FileMiME
import com.alibaba.fastjson.JSONObject
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap


/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/10/28 16:38
 */
@Service

class ClientService(private val imgAndAlbumService: ImgAndAlbumService,
                    private val nOSImageupload: NOSImageupload,
                    private val ossImageupload: OSSImageupload,
                    private val ussImageupload: USSImageupload,
                    private val kodoImageupload: KODOImageupload,
                    private val cosImageupload: COSImageupload,
                    private val sysConfigService: SysConfigService,
                    private val ftpImageupload: FTPImageupload,
                    private val uFileImageupload: UFileImageupload,
                    private val userMapper: UserMapper,
                    private val keysMapper: KeysMapper,
                    private val configMapper: ConfigMapper,
                    private val uploadConfigMapper: UploadConfigMapper,
                    private val imgMapper: ImgMapper,
                    var imgreviewMapper: ImgreviewMapper,) {

    fun uploadImg(request: HttpServletRequest?, multipartFile: MultipartFile?, email: String?, pass: String?): Msg {
        val msg = Msg()
        return try {
            val sourceKeyId: String
            var fis: FileInputStream? = null
            var md5key: String? = null
            val setday = 0
            val jsonObject = JSONObject()
            val config = configMapper!!.sourceype
            val userip = getIpAddr(request)
            val uploadConfig = uploadConfigMapper!!.updateConfig
            if (uploadConfig.api != 1) {
                msg.code = "4003"
                msg.info = "??????????????????API??????"
                return msg
            }
            val file = changeFile_new(multipartFile!!)
            val u2 = SysUser()
            if (!file!!.exists() || email == null || pass == null) {
                msg.code = "4005"
                msg.info = "????????????????????????"
                return msg
            }
            u2.email = email
            u2.password = Base64Encryption.encryptBASE64(pass.toByteArray())
            val u = userMapper!!.getUsers(u2)
            //???????????????????????????????????????????????????
            if (null == u || u.isok != 1) {
                msg.code = "4006"
                msg.info = "?????????????????????,????????????"
                return msg
            }
            val imguid = UUID.randomUUID().toString().replace("-", "")
            //??????????????????????????????????????????
            val msg1 = updateImgCheck(u, uploadConfig)
            if (msg1.code != "300") {
                return msg1
            }
            sourceKeyId = siteGroup!!.keyID
            val key = keysMapper!!.selectKeys(sourceKeyId)
            val tmp = if (memory == -1L) -2 else UsedTotleMemory!!
            if (tmp >= memory!!) {
                msg.code = "4007"
                msg.info = if (u == null) "?????????????????????" else "????????????????????????"
                return msg
            }
            if (file.length() > TotleMemory!!) {
                System.err.println("???????????????" + file.length())
                System.err.println("???????????????" + TotleMemory)
                msg.code = "4008"
                msg.info = "??????????????????????????????"
                return msg
            }
            try {
                fis = FileInputStream(file)
                md5key = DigestUtils.md5Hex(fis)
            } catch (e: Exception) {
                e.printStackTrace()
                println("?????????????????????MD5,??????UUID")
            }
            val fileMiME = FileMiME(file)
            if (fileMiME.code != "200") {
                //???????????????
                msg.code = "4009"
                msg.info = fileMiME.info
                return msg
            }
            if (md5key == null || md5key == "") {
                md5key = UUID.randomUUID().toString().replace("-", "")
            }
            val prefix = file.name.substring(file.name.lastIndexOf(".") + 1)
            if (uploadConfig.blacklist != null) {
                val iparr = uploadConfig.blacklist.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (s in iparr) {
                    if (s == userip) {
                        msg.code = "4010"
                        msg.info = "?????????????????????"
                        return msg
                    }
                }
            }
            //????????????????????????
            if (Integer.valueOf(sysConfigService!!.getstate().checkduplicate) == 1) {
                val imaOBJ = Images()
                imaOBJ.md5key = md5key
                imaOBJ.userId = u.id
                if (imgMapper!!.md5Count(imaOBJ) > 0) {
                    val images = imgMapper.selectImgUrlByMD5(md5key)
                    jsonObject["url"] = images.imgUrl
                    jsonObject["name"] = file.name
                    jsonObject["size"] = images.sizes
                    msg.data = jsonObject
                    return msg
                }
            }
            val map: MutableMap<String, File?> = HashMap()
            val fileName = file.name
            if (file.exists()) {
                map[prefix] = file
            }
            val stime = System.currentTimeMillis()
            val m: Map<ReturnImage, Int>? = null
            val returnImage = storageSource(key.storageType, map, updatePath, key.id)
            val img = Images()
            val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            if (returnImage!!.code == "200") {
                val imgurl = returnImage.imgUrl
                val imgsize = returnImage.imgSize
                val imgname = returnImage.imgName
                img.imgUrl = imgurl
                img.updateTime = LocalDateTime.now()
                img.source = key.id
                img.userId = if (u == null) "0" else u.id
                img.sizes = imgsize.toInt()
                if (uploadConfig.urlType == 2) {
                    img.imgName = imgname
                } else {
                    img.imgName = getSubString(imgname, key.requestAddress + "/", "")
                }
                img.imgType = if (setday > 0) 1 else 0
                img.abnormal = userip
                img.md5key = md5key
                img.imgUid = imguid
                img.format = fileMiME.data.toString()
                imgMapper!!.insert(img)
                val etime = System.currentTimeMillis()
                Print.Normal("??????????????????????????????" + (etime - stime).toString() + "ms")
                jsonObject["url"] = img.imgUrl
                jsonObject["name"] = imgname
                jsonObject["size"] = img.sizes
                //??????????????????
                Thread { LegalImageCheck(img) }.start()
            } else {
                msg.code = "5001"
                msg.info = "????????????????????????"
                return msg
            }
            file.delete()
            msg.data = jsonObject
            //            ???????????????=========
            msg
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "5001"
            msg.info = "Error for server:500"
            msg
        }
    }

    //???????????? ??? ?????? ????????????????????????????????????
    fun updateImgCheck(sysUser: SysUser?, uploadConfig: UploadConfig): Msg {
        val msg = Msg()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        try {
            if (sysUser == null) {
                //?????????????????????????????????????????????????????????
                if (uploadConfig.isUpdate != 1) {
                    msg.code = "1000"
                    msg.info = "???????????????????????????"
                    return msg
                }
                siteGroup = GetCurrentSource.GetSource(null)
                memory = java.lang.Long.valueOf(uploadConfig.visitorStorage) //?????? B ??????????????????
                TotleMemory = java.lang.Long.valueOf(uploadConfig.fileSizeTourists) //?????? B  ?????????????????????
                UsedTotleMemory =
                    if (imgMapper!!.getUserMemory("0") == null) 0L else imgMapper.getUserMemory("0") //?????? B
            } else {
                //???????????????????????????
                if (uploadConfig.userclose != 1) {
                    msg.code = "1001"
                    msg.info = "???????????????????????????"
                    return msg
                }
                updatePath = sysUser.username
                siteGroup = GetCurrentSource.GetSource(sysUser.id)
                memory = java.lang.Long.valueOf(sysUser.memory) * 1024 * 1024 //?????? B
                TotleMemory = java.lang.Long.valueOf(uploadConfig.fileSizeUser) //?????? B
                UsedTotleMemory =
                    if (imgMapper!!.getUserMemory(sysUser.id) == null) 0L else imgMapper.getUserMemory(sysUser.id) //?????? B
            }
            //???????????????????????????????????????
            if (uploadConfig.urlType == 2) {
                updatePath = dateFormat.format(LocalDateTime.now())
            }
            msg.code = "300"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "500"
        }
        return msg
    }

    //????????????
    @Synchronized
    fun LegalImageCheck(images: Images) {
        println("??????????????????????????????")
        var imgreview: Imgreview? = null
        try {
            imgreview = imgreviewMapper!!.selectByusing(1)
        } catch (e: Exception) {
            Print.warning("???????????????????????????????????????")
            e.printStackTrace()
        }
        LegalImageCheckForBaiDu(imgreview, images)
    }

    fun LegalImageCheckForBaiDu(imgreview: Imgreview?, images: Images) {
        if (imgreview!!.using == 1) {
            try {
                val client = AipContentCensor(imgreview.appId, imgreview.apiKey, imgreview.secretKey)
                client.setConnectionTimeoutInMillis(5000)
                client.setSocketTimeoutInMillis(30000)
                var res = client.imageCensorUserDefined(images.imgUrl, EImgType.FILE, null)
                res = client.imageCensorUserDefined(images.imgUrl, EImgType.URL, null)
                val jsonArray = JSON.parseArray("[$res]")
                for (o in jsonArray) {
                    val jsonObject = o as JSONObject
                    val data = jsonObject.getJSONArray("data")
                    val conclusionType = jsonObject.getInteger("conclusionType")
                    if (conclusionType != null) {
                        if (conclusionType == 2) {
                            for (datum in data) {
                                val imgdata = datum as JSONObject
                                if (imgdata.getInteger("type") == 1) {
                                    val img = Images()
                                    img.imgName = images.imgName
                                    //??????????????????????????????ID???????????????????????????????????????????????????
                                    img.violation = "1[1]"
                                    imgMapper!!.setImg(img)
                                    val imgv = Imgreview()
                                    imgv.id = "1"
                                    val count = imgreview.count
                                    println("?????????????????????$count")
                                    imgv.count = count + 1
                                    imgreviewMapper!!.updateByPrimaryKeySelective(imgv)
                                    System.err.println("???????????????????????????????????????")
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("?????????????????????????????????????????????")
                e.printStackTrace()
            }
        }
    }

    companion object {
        var siteGroup //????????????????????????????????????
                : SiteGroup? = null
        var memory //??????????????????????????????????????? memory
                : Long? = null
        var TotleMemory //?????????????????????????????????????????? //maxsize
                : Long? = null
        var UsedTotleMemory //?????????????????????????????????????????? //usermemory
                : Long? = null
        var updatePath = "tourist"
    }
}