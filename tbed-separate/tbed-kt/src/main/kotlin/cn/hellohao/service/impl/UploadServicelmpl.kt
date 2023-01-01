package cn.hellohao.service.impl

import cn.hellohao.mapper.*
import cn.hellohao.model.entity.*

import cn.hellohao.model.vo.UploadImgVo
import cn.hellohao.service.ImgTempService
import cn.hellohao.service.SysConfigService
import cn.hellohao.util.GetCurrentSource
import cn.hellohao.util.GetIPS
import cn.hellohao.util.ImgUrlUtil.downLoadFromUrl
import cn.hellohao.util.ImgUrlUtil.getFileLength
import cn.hellohao.util.Print
import cn.hellohao.util.SetFiles.changeFile_new
import cn.hellohao.util.SetText.shortUuid
import cn.hellohao.util.TypeDict.FileMiME
import cn.hutool.core.lang.Console
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.baidu.aip.contentcensor.AipContentCensor
import com.baidu.aip.contentcensor.EImgType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.shiro.SecurityUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/1/9 15:46
 */
@Service
class UploadServicelmpl(
    var configMapper: ConfigMapper,
    var sysConfigService: SysConfigService,
    var uploadConfigMapper: UploadConfigMapper,
    var keysMapper: KeysMapper,
    var imgMapper: ImgMapper,
    var userMapper: UserMapper,
    var imgreviewMapper: ImgreviewMapper,
    var imgTempService: ImgTempService
) {

    fun uploadForLoc(
        request: HttpServletRequest?,
        multipartFile: MultipartFile?, setday: Int, imgUrl: String?, selectTreeList: JSONArray?
    ): Msg {
        val msg = Msg()
        return try {
            val jsonObject = UploadImgVo()
            val uploadConfig = uploadConfigMapper!!.updateConfig
            val userIp = GetIPS.getIpAddr(request)
            val subject = SecurityUtils.getSubject()
            var u: SysUser? = subject.principal as SysUser
            if (null != u) {
                u = userMapper!!.getUsers(u)
            }
            val sourceKeyId: String
            var md5key: String? = null
            val fis: FileInputStream
            val file: File?
            file = if (imgUrl == null) {
                changeFile_new(multipartFile!!)
            } else {
                //说明是URL上传
                val imgData: Msg = UploadServicelmpl.Companion.uploadForURL(request, imgUrl)
                if (imgData.code == "200") {
                    File(imgData.data as String)
                } else {
                    return imgData
                }
            }
            val imgUid = UUID.randomUUID().toString().replace("-", "")
            //判断上传前的一些用户限制信息
            val msg1 = updateImgCheck(u, uploadConfig)
            if (msg1.code != "300") {
                return msg1
            }

            //判断可用容量
            sourceKeyId = UploadServicelmpl.Companion.siteGroup.getKeyID()
            val key = keysMapper!!.selectKeys(sourceKeyId)
            val tmp: Long =
                if (UploadServicelmpl.Companion.memory == -1L) -2 else UploadServicelmpl.Companion.UsedTotleMemory
            if (tmp >= UploadServicelmpl.Companion.memory) {
                msg.code = "4005"
                msg.info = if (u == null) "游客空间已用尽" else "您的可用空间不足"
                return msg
            }

            //判断图片有没有超出设定大小
            if (file!!.length() > UploadServicelmpl.Companion.TotleMemory) {
                System.err.println("文件大小：" + file.length())
                System.err.println("最大限制：" + UploadServicelmpl.Companion.TotleMemory)
                msg.code = "4006"
                msg.info = "图像超出系统限制大小"
                return msg
            }
            try {
                fis = FileInputStream(file)
                md5key = DigestUtils.md5Hex(fis)
            } catch (e: Exception) {
                e.printStackTrace()
                println("未获取到图片的MD5,成成UUID")
            }
            val fileMiME = FileMiME(file)
            if (fileMiME.code != "200") {
                //非图像文本
                msg.code = "4000"
                msg.info = fileMiME.info
                return msg
            }
            if (md5key == null || md5key == "") {
                md5key = UUID.randomUUID().toString().replace("-", "")
            }

            //判断图片是否存在
            if (sysConfigService!!.getstate().checkduplicate.toInt() == 1) {
                val imaOBJ = Images()
                imaOBJ.md5key = md5key
                imaOBJ.userId = if (u == null) "0" else u.id
                if (imgMapper!!.md5Count(imaOBJ) > 0) {
                    val images = imgMapper!!.selectImgUrlByMD5(md5key)
                    jsonObject.url = images.imgUrl
                    jsonObject.name = file.name
                    jsonObject.imgUid = images.imgUid
                    //                    jsonObject.put("shortLink",images.getShortlink());
                    msg.data = jsonObject
                    return msg
                }
            }
            val prefix = file.name.substring(file.name.lastIndexOf(".") + 1)
            //判断黑名单
            if (uploadConfig.blacklist != null) {
                val iparr = uploadConfig.blacklist.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (s in iparr) {
                    if (s == userIp) {
                        file.delete()
                        msg.code = "4003"
                        msg.info = "你暂时不能上传"
                        return msg
                    }
                }
            }
            val map: MutableMap<String, File?> = HashMap()
            if (file.exists()) {
                map[prefix] = file
            }
            val stime = System.currentTimeMillis()
            val m: Map<ReturnImage, Int>? = null
            val returnImage = storageSource(key.storageType, map, UploadServicelmpl.Companion.updatePath, key.id)
            val img = Images()
            if (returnImage!!.code == "200") {
                val imgurl = returnImage.imgUrl
                val imgsize = returnImage.imgSize
                val imgname = returnImage.imgName
                img.imgUrl = imgurl
                img.updateTime = LocalDateTime.now()
                img.createTime = LocalDateTime.now()
                img.source = key.id
                img.userId = if (u == null) "0" else u.id
                img.sizes = imgsize.toInt()
                if (uploadConfig.urlType == 2) {
                    img.imgName = imgname
                } else {
                    img.imgName = getSubString(imgname, key.requestAddress + "/", "")
                }
                if (setday == 1 || setday == 3 || setday == 7 || setday == 30) {
                    img.imgType = 1
                    val imgDataExp = ImgTemp()
                    imgDataExp.delTime = Timestamp.valueOf(UploadServicelmpl.Companion.plusDay(setday))
                    imgDataExp.imgUid = imgUid
                    imgTempService!!.insertImgExp(imgDataExp)
                } else {
                    img.imgType = 0
                }
                img.abnormal = userIp
                img.md5key = md5key
                img.imgUid = imgUid
                img.format = fileMiME.data.toString()
                imgMapper!!.insert(img)
                val etime = System.currentTimeMillis()
                Print.Normal("上传图片所用总时长：" + (etime - stime).toString() + "ms")
                jsonObject.url = img.imgUrl
                jsonObject.name = imgname
                jsonObject.imgUid = img.imgUid
                //                jsonObject.put("shortLink", img.getShortlink());
                Thread { LegalImageCheck(img) }.start()
            } else {
                msg.code = "5001"
                msg.info = "上传服务内部错误"
                return msg
            }
            file.delete()
            msg.data = jsonObject
            msg
        } catch (e: Exception) {
            e.printStackTrace()
            msg.info = "上传时发生了一些错误"
            msg.code = "110500"
            msg
        }
    }

    //判断用户 或 游客 当前上传图片的一系列校验
    private fun updateImgCheck(sysUser: SysUser?, uploadConfig: UploadConfig): Msg {
        val msg = Msg()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        try {
            if (sysUser == null) {
                //用户没有登陆，值判断游客能不能上传即可
                if (uploadConfig.isUpdate != 1) {
                    msg.code = "1000"
                    msg.info = "系统已禁用游客上传"
                    return msg
                }
                UploadServicelmpl.Companion.siteGroup = GetCurrentSource.GetSource(null)
                //单位 B 游客设置总量
                UploadServicelmpl.Companion.memory = java.lang.Long.valueOf(uploadConfig.visitorStorage)
                //单位 B  游客单文件大小
                UploadServicelmpl.Companion.TotleMemory = java.lang.Long.valueOf(uploadConfig.fileSizeTourists)
                //单位 B
                UploadServicelmpl.Companion.UsedTotleMemory =
                    if (imgMapper!!.getUserMemory("0") == null) 0L else imgMapper!!.getUserMemory("0")
            } else {
                //判断用户能不能上传
                if (uploadConfig.userclose != 1) {
                    msg.code = "1001"
                    msg.info = "系统已禁用上传功能"
                    return msg
                }
                UploadServicelmpl.Companion.updatePath = sysUser.username
                UploadServicelmpl.Companion.siteGroup = GetCurrentSource.GetSource(sysUser.id)
                //单位 B
                UploadServicelmpl.Companion.memory = sysUser.memory * 1024 * 1024
                //单位 B
                UploadServicelmpl.Companion.TotleMemory = java.lang.Long.valueOf(uploadConfig.fileSizeUser)
                //单位 B
                UploadServicelmpl.Companion.UsedTotleMemory =
                    if (imgMapper!!.getUserMemory(sysUser.id) == null) 0L else imgMapper!!.getUserMemory(sysUser.id)
            }
            if (uploadConfig.urlType == 2) {
                UploadServicelmpl.Companion.updatePath = dateFormat.format(LocalDateTime.now())
            }
            msg.code = "300"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "500"
        }
        return msg
    }

    @Synchronized
    private fun LegalImageCheck(images: Images) {
        println("非法图像鉴别进程启动")
        var imgreview: Imgreview? = null
        try {
            imgreview = imgreviewMapper!!.selectByusing(1)
        } catch (e: Exception) {
            Print.warning("获取鉴别程序的时候发生错误")
            e.printStackTrace()
        }
        imgreview?.let { LegalImageCheckForBaiDu(it, images) }
    }

    private fun LegalImageCheckForBaiDu(imgreview: Imgreview, images: Images) {
        println("非法图像鉴别进程启动-BaiDu")
        if (imgreview.using == 1) {
            try {
                val client = AipContentCensor(imgreview.appId, imgreview.apiKey, imgreview.secretKey)
                client.setConnectionTimeoutInMillis(5000)
                client.setSocketTimeoutInMillis(30000)
                var res = client.imageCensorUserDefined(images.imgUrl, EImgType.FILE, null)
                res = client.imageCensorUserDefined(images.imgUrl, EImgType.URL, null)
                System.err.println("返回的鉴黄json:$res")
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
                                    img.violation = "1[1]"
                                    imgMapper!!.setImg(img)
                                    val imgv = Imgreview()
                                    imgv.id = "1"
                                    val count = imgreview.count
                                    println("违法图片总数：$count")
                                    imgv.count = count + 1
                                    imgreviewMapper!!.updateByPrimaryKeySelective(imgv)
                                    System.err.println("存在非法图片，进行处理操作")
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("图像鉴黄线程执行过程中出现异常")
                e.printStackTrace()
            }
        }
    }

    companion object {
        //通过图片Url上传图片
        fun uploadForURL(request: HttpServletRequest, imgurl: String?): Msg {
            val msg = Msg()
            //先判断是不是有效链接
//        final boolean valid = ImgUrlUtil.isValid(imgurl);
            if (true) {
                var imgsize: Long? = null
                try {
                    imgsize = getFileLength(imgurl)
                    if (imgsize > 0) {
//                    String uuid= UUID.randomUUID().toString().replace("-", "");
                        val ShortUID = shortUuid
                        val savePath =
                            request.session.servletContext.getRealPath("/") + File.separator + "hellohaotmp" + File.separator
                        val bl = downLoadFromUrl(imgurl, ShortUID, savePath)
                        if (bl["res"] as Boolean? == true) {
//                        File file = new File();
                            msg.code = "200"
                            msg.data = bl["imgPath"] //savePath + File.separator + ShortUID
                            return msg
                        } else {
                            if (bl["StatusCode"] == "110403") {
                                msg.info = "该链接非图像文件，无法上传"
                            } else {
                                msg.info = "该链接暂时无法上传"
                            }
                            msg.code = "500"
                        }
                    } else {
                        msg.code = "500"
                        msg.info = "获取资源失败"
                    }
                } catch (e: IOException) {
                    msg.code = "500"
                    msg.info = "获取资源失败"
                }
            } else {
                msg.code = "500"
                msg.info = "该链接无效"
            }
            return msg
        }

        /**
         * 上传用户或游客的所属分组
         */
        var siteGroup: SiteGroup? = null

        /**
         * 上传用户或者游客的分配容量 memory
         */
        var memory: Long? = null

        /**
         * //用户或者游客下可使用的总容量 //maxsize
         */
        var TotleMemory: Long? = null

        //用户或者游客已经用掉的总容量 //usermemory
        var UsedTotleMemory: Long? = null
        var updatePath = "tourist"

        //计算时间
        fun plusDay(setday: Int): LocalDateTime {
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val today = LocalDateTime.now()
            Console.log("现在的日期是：$today")
            val enddate = today.plusDays(setday.toLong())
            Console.log("到期的日期：" + enddate.format(format))
            return enddate
        }
    }
}