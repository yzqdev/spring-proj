package cn.hellohao.controller

import cn.hellohao.auth.token.JWTUtil
import cn.hellohao.model.entity.Config
import cn.hellohao.model.entity.StorageKey
import cn.hellohao.service.*
import cn.hellohao.service.impl.*
import cn.hellohao.service.implimport.NOSImageupload

import cn.hellohao.util.verifyCode.IVerifyCodeGen
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.apache.shiro.SecurityUtils
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.lang.Boolean
import kotlin.Any
import kotlin.Exception
import kotlin.Int
import kotlin.String

@RestController
class IndexController(
    private val imgService: ImgService,


    private val sysConfigService: SysConfigService,


    var iRedisService: IRedisService,


    private val configService: ConfigService,


    private val uploadConfigService: UploadConfigService,


    private val uploadServicelmpl: UploadServicelmpl,


    private val nosImageupload: NOSImageupload,


    private val ossImageupload: OSSImageupload,


    private val cosImageupload: COSImageupload,


    private val kodoImageupload: KODOImageupload,


    private val ussImageupload: USSImageupload,


    private val uFileImageupload: UFileImageupload,


    private val ftpImageupload: FTPImageupload,


    var albumService: AlbumService,


    private val keysService: KeysService,


    private val imgTempService: ImgTempService,


    private val imgAndAlbumService: ImgAndAlbumService,) {

    @GetMapping(value = ["/webInfo"])
    fun webInfo(): Msg {
        val msg = Msg()
        val config: Config = configService.getSourceType()
        val updateConfig: UploadConfig = uploadConfigService.getUpdateConfig()
        val sysConfig: SysConfig = sysConfigService.getstate()
        val jsonObject = JSONObject()
        jsonObject["webname"] = config.getWebname()
        jsonObject["websubtitle"] = config.getWebsubtitle()
        jsonObject["keywords"] = config.getWebkeywords()
        jsonObject["description"] = config.getWebms()
        jsonObject["explain"] = config.getExplain()
        jsonObject["favicon"] = config.getWebfavicons()
        jsonObject["baidu"] = config.getBaidu()
        jsonObject["links"] = config.getLinks()
        jsonObject["aboutinfo"] = config.getAboutInfo()
        jsonObject["logo"] = config.getLogo()
        jsonObject["api"] = updateConfig.getApi()
        jsonObject["register"] = sysConfig.getRegister()
        msg.data = jsonObject
        return msg
    }

    @PostMapping(value = ["/upload"]) //upimg new
    fun upimg(
        @RequestParam(value = "file", required = false) multipartFile: MultipartFile?, day: Int,
        @RequestParam(value = "classifications", defaultValue = "") classifications: String
    ): Msg {
        val jsonArray = JSONArray()
        val request: HttpServletRequest = RequestHelper.getRequest()
        if (classifications != "") {
            val calssif = classifications.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in calssif.indices) {
                jsonArray.add(calssif[i])
            }
        }
        return uploadServicelmpl.uploadForLoc(request, multipartFile, day, null, jsonArray)
    }

    //??????????????????url??????
    @PostMapping(value = ["/uploadForUrl"]) //new
    fun upurlimg(@RequestBody data: Map<String?, Any?>): Msg {
        val jsonArray = JSONArray()
        val setday = data["day"] as Int
        val imgUrl = data["imgUrl"] as String?
        val selectTreeListStr = data["classifications"] as String?
        if (null != selectTreeListStr) {
            val calssif = selectTreeListStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in calssif.indices) {
                jsonArray.add(calssif[i])
            }
        }
        return uploadServicelmpl.uploadForLoc(RequestHelper.getRequest(), null, setday, imgUrl, jsonArray)
    }

    //new
    val uploadInfo: Msg
        @GetMapping(value = ["/getUploadInfo"]) get() {
            val msg = Msg()
            val jsonObject = JSONObject()
            val subject = SecurityUtils.getSubject()
            val sysUser: SysUser = subject.principal as SysUser
            try {
                val updateConfig: UploadConfig = uploadConfigService.getUpdateConfig()
                jsonObject["suffix"] = updateConfig.getSuffix().split(",")
                if (null == sysUser) {
                    jsonObject["filesize"] = Integer.valueOf(updateConfig.getFileSizeTourists()) / 1024
                    jsonObject["imgcount"] = updateConfig.getImgCountTourists()
                    jsonObject["uploadSwitch"] = updateConfig.getIsUpdate()
                    jsonObject["uploadInfo"] = "????????????????????????????????????"
                } else {
                    jsonObject["filesize"] = Integer.valueOf(updateConfig.getFileSizeUser()) / 1024
                    jsonObject["imgcount"] = updateConfig.getImgCountUser()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            msg.data = jsonObject
            return msg
        }

    @PostMapping("/checkStatus")
    fun checkStatus(request: HttpServletRequest): Msg {
        val msg = Msg()
        val token: String = request.getHeader("Authorization")
        if (token != null) {
            val tokenJson: UserClaim = JWTUtil.checkToken(token)
            if (Boolean.TRUE == tokenJson.getCheck()) {
                val subject = SecurityUtils.getSubject()
                val tokenOBJ = UsernamePasswordToken(tokenJson.getEmail(), tokenJson.getPassword())
                tokenOBJ.setRememberMe(true)
                try {
                    subject.login(tokenOBJ)
                    SecurityUtils.getSubject().session.timeout = 3600000
                    val u: SysUser = subject.principal as SysUser
                    val jsonObject = JSONObject()
                    jsonObject["RoleLevel"] = if (u.getLevel() === 2) "admin" else "user"
                    jsonObject["userName"] = u.getUsername()
                    msg.code = "200"
                    msg.data = jsonObject
                } catch (e: Exception) {
                    msg.code = "40041"
                    msg.info = "??????????????????????????????"
                    System.err.println("??????????????????????????????")
                    e.printStackTrace()
                }
            } else {
                msg.code = "40041"
                msg.info = "??????????????????????????????"
            }
        } else {
            msg.code = "40040"
            msg.info = "??????????????????????????????"
        }
        return msg
    }

    @GetMapping("/verifyCode")
    fun verifyCode(request: HttpServletRequest?, response: HttpServletResponse, httpSession: HttpSession?) {
        val iVerifyCodeGen: IVerifyCodeGen = SimpleCharVerifyCodeGenImpl()
        try {
            //?????????????????????
            val verifyCode: VerifyCode = iVerifyCodeGen.generate(80, 38)
            val code: String = verifyCode.getCode()
            val userIP: String = GetIPS.getIpAddr(request)
            iRedisService.setValue(userIP + "_hellohao_verifyCode", code)
            response.setHeader("Pragma", "no-cache")
            response.setHeader("Cache-Control", "no-cache")
            response.setDateHeader("Expires", 0)
            response.setContentType("image/jpeg")
            response.getOutputStream().write(verifyCode.getImgBytes())
            response.getOutputStream().flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @GetMapping("/verifyCodeForRegister")
    fun verifyCodeForRegister(request: HttpServletRequest?, response: HttpServletResponse, httpSession: HttpSession?) {
        val iVerifyCodeGen: IVerifyCodeGen = SimpleCharVerifyCodeGenImpl()
        try {
            val verifyCode: VerifyCode = iVerifyCodeGen.generate(80, 38)
            val code: String = verifyCode.getCode()
            val userIP: String = GetIPS.getIpAddr(request)
            iRedisService.setValue(userIP + "_hellohao_verifyCodeForRegister", code)
            response.setHeader("Pragma", "no-cache")
            response.setHeader("Cache-Control", "no-cache")
            response.setDateHeader("Expires", 0)
            response.setContentType("image/jpeg")
            response.getOutputStream().write(verifyCode.getImgBytes())
            response.getOutputStream().flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @GetMapping("/verifyCodeForRetrieve")
    fun verifyCodeForRetrieve(request: HttpServletRequest?, response: HttpServletResponse, httpSession: HttpSession) {
        val iVerifyCodeGen: IVerifyCodeGen = SimpleCharVerifyCodeGenImpl()
        try {
            val verifyCode: VerifyCode = iVerifyCodeGen.generate(80, 38)
            val code: String = verifyCode.getCode()
            println("verifyCodeForRetrieve-zhaoHui httpSession ID===" + httpSession.getId())
            val userIP: String = GetIPS.getIpAddr(request)
            iRedisService.setValue(userIP + "_hellohao_verifyCodeForEmailRetrieve", code)
            response.setHeader("Pragma", "no-cache")
            response.setHeader("Cache-Control", "no-cache")
            response.setDateHeader("Expires", 0)
            response.setContentType("image/jpeg")
            response.getOutputStream().write(verifyCode.getImgBytes())
            response.getOutputStream().flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //????????????
    @PostMapping("/deleImagesByUid/{imgUid}") //new
    fun deleImagesByUid(@PathVariable("imgUid") imgUid: String?): Msg {
        val msg = Msg()
        val image: Images = imgService.selectImgUrlByImgUID(imgUid)
        val subject = SecurityUtils.getSubject()
        val sysUser: SysUser = subject.principal as SysUser
        if (null != sysUser) {
            if (!sysUser.getId().equals(image.getUserId())) {
                msg.info = "????????????????????????????????????????????????"
                msg.code = "100403"
                return msg
            }
        }
        val keyID: String = image.getSource()
        val imgname: String = image.getImgName()
        val key: StorageKey = keysService.selectKeys(keyID)
        //????????????
        var isDele = false
        if (key.getStorageType() === 1) {
            isDele = nosImageupload.delNOS(key.getId(), imgname)
        } else if (key.getStorageType() === 2) {
            isDele = ossImageupload.delOSS(key.getId(), imgname)
        } else if (key.getStorageType() === 3) {
            isDele = ussImageupload.delUSS(key.getId(), imgname)
        } else if (key.getStorageType() === 4) {
            isDele = kodoImageupload.delKODO(key.getId(), imgname)
        } else if (key.getStorageType() === 5) {
            isDele = LocUpdateImg.deleteLOCImg(imgname)
        } else if (key.getStorageType() === 6) {
            isDele = cosImageupload.delCOS(key.getId(), imgname)
        } else if (key.getStorageType() === 7) {
            isDele = ftpImageupload.delFTP(key.getId(), imgname)
        } else if (key.getStorageType() === 8) {
            isDele = uFileImageupload.delUFile(key.getId(), imgname)
        } else {
            System.err.println("????????????????????????????????????????????????")
        }
        //?????????
        if (isDele) {
            try {
                imgAndAlbumService.deleteImgAndAlbum(imgname)
                imgTempService.delImgAndExp(image.getImgUid())
                imgService.deleteImgById(image.getId())
            } catch (e: Exception) {
                e.printStackTrace()
                msg.info = "???????????????????????????"
                msg.code = "500"
                return msg
            }
            msg.info = "????????????"
        } else {
            imgAndAlbumService.deleteImgAndAlbum(imgname)
            imgTempService.delImgAndExp(image.getImgUid())
            imgService.deleteImgById(image.getId())
            msg.info = "???????????????????????????????????????????????????"
            msg.code = "500"
        }
        println("???????????????" + msg.toString())
        return msg
    }

    //????????????
    @PostMapping("/authError")
    fun authError(request: HttpServletRequest?): Msg {
        val msg = Msg()
        msg.code = "4031"
        msg.info = "You don't have authority"
        return msg
    }

    //????????????
    @PostMapping("/jurisError")
    fun jurisError(request: HttpServletRequest?): Msg {
        val msg = Msg()
        msg.code = "4031"
        msg.info = "Authentication request failed"
        return msg
    }
}