package cn.hellohao.controller

import cn.hellohao.model.dto.SysUserUpdateDto
import cn.hellohao.model.dto.UserSearchDto
import cn.hellohao.model.entity.Msg
import cn.hellohao.model.entity.SysUser
import cn.hellohao.service.*
import cn.hellohao.serviceimport.ImgreviewService
import cn.hellohao.serviceimport.KeysService
import cn.hellohao.serviceimport.UserService
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import lombok.RequiredArgsConstructor
import org.apache.shiro.SecurityUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/admin/root")

class AdminRootController( private val configService: ConfigService, private val keysService: KeysService, private val emailConfigService: EmailConfigService, private val uploadConfigService: UploadConfigService, private val sysConfigService: SysConfigService, private val imgService: ImgService, private val imgreviewService: ImgreviewService) {


    @PostMapping(value = ["/getUserList"]) //new
    @ResponseBody
    fun getUserList(@RequestBody userSearchDto: UserSearchDto): Map<String, Any> {
        val pageNum: Int = userSearchDto.pageNum
        val pageSize: Int = userSearchDto.pageSize
        val queryText: String = userSearchDto.queryText
        val page = Page<SysUser>(pageNum.toLong(), pageSize.toLong())
        val users: Page<SysUser> = userService.getUserListByName(page, queryText)
        val map: MutableMap<String, Any> = HashMap(2)
        map["count"] = users.total
        map["users"] = users.records
        return map
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateDto 用户更新dto
     * @return [Msg]
     */
    @PostMapping(value = ["/updateUserInfo"])
    @ResponseBody
    fun updateUserInfo(@RequestBody userUpdateDto: SysUserUpdateDto): Msg {
        val msg = Msg()
        try {
            val subject = SecurityUtils.getSubject()
            val u = subject.principal as SysUser
            val id: String = userUpdateDto.getId()
            val email: String = userUpdateDto.getEmail()
            val memory: Long = userUpdateDto.getMemory()
            val groupid: String = userUpdateDto.getGroupId()
            val isok: Int = userUpdateDto.getIsok()
            if (memory < 0 || memory > 1048576L) {
                msg.code = "500"
                msg.info = "容量不得超过1048576"
                return msg
            }
            val sysUser = SysUser()
            val sysUser2 = SysUser()
            sysUser2.setId(id)
            val sysUserInfo: SysUser = userService.getUsers(sysUser2)
            sysUser.setId(id)
            sysUser.setEmail(email)
            sysUser.setMemory(memory * 1024 * 1024)
            sysUser.setGroupId(groupid)
            if (sysUserInfo.getLevel() === 1) {
                sysUser.setIsok(if (isok == 1) 1 else -1)
            }
            userService.changeUser(sysUser)
            msg.info = "修改成功"
        } catch (e: Exception) {
            msg.code = "500"
            msg.info = "修改失败"
            e.printStackTrace()
        }
        return msg
    }

    /**
     * 禁用用户
     *
     * @param userIdList 用户id列表
     * @return [Msg]
     */
    @PostMapping("/disableUser")
    @ResponseBody
    fun disableUser(@RequestBody userIdList: Array<String?>): Msg {
        val msg = Msg()
        try {
            for (s in userIdList) {
                val u = SysUser()
                u.setId(s)
                val u2: SysUser = userService.getUsers(u)
                if (u2.getLevel() === 1) {
                    val sysUser = SysUser()
                    sysUser.setId(s)
                    sysUser.setIsok(-1)
                    userService.changeUser(sysUser)
                }
            }
            msg.info = "所选用户已被禁用"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.info = "系统错误"
            msg.code = "500"
        }
        return msg
    }

    @PostMapping("/deleUser") //new
    @ResponseBody
    fun deleuser(@RequestBody userIdList: Array<String?>): Msg {
        val msg = Msg()
        try {
            var b = false
            for (i in userIdList.indices) {
                val u = SysUser()
                u.setId(userIdList[i])
                val sysUser: SysUser = userService.getUsers(u)
                if (sysUser.getLevel() === 1) {
                    userService.deleteUserById(userIdList[i])
                } else {
                    b = true
                }
            }
            if (b && userIdList.size == 1) {
                msg.info = "管理员账户不可删除"
            } else {
                msg.info = "用户已删除成功"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            msg.info = "系统错误"
            msg.code = "500"
        }
        return msg
    }

    val keysList: Msg
        @PostMapping("/getKeysList") @ResponseBody get() {
            val msg = Msg()
            val list: List<StorageKey> = keysService.getKeys()
            msg.data = list
            return msg
        }

    @PostMapping("/LoadInfo/{keyId}") //new
    @ResponseBody
    fun LoadInfo(@PathVariable("keyId") keyId: String?): Msg {
        val msg = Msg()
        try {
            val jsonObject = JSONObject()
            jsonObject["id"] = keyId
            val key: StorageKey = keysService.selectKeys(keyId)
            var ret = 0
            if (key.getStorageType() === 1) {
                ret = NOSImageupload.Initialize(key)
            } else if (key.getStorageType() === 2) {
                ret = OSSImageupload.Initialize(key)
            } else if (key.getStorageType() === 3) {
                ret = USSImageupload.Initialize(key)
            } else if (key.getStorageType() === 4) {
                ret = KODOImageupload.Initialize(key)
            } else if (key.getStorageType() === 6) {
                ret = COSImageupload.Initialize(key)
            } else if (key.getStorageType() === 7) {
                ret = FTPImageupload.Initialize(key)
            } else if (key.getStorageType() === 8) {
                ret = UFileImageupload.Initialize(key)
            }
            val l: Long = imgService.getSourceMemory(keyId)
            jsonObject["isok"] = ret
            jsonObject["storagetype"] = key.getStorageType()
            if (l == null) {
                jsonObject["usedCapacity"] = 0
            } else {
                jsonObject["usedCapacity"] = readableFileSize(l)
            }
            msg.data = jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "500"
        }
        return msg
    }

    @PostMapping("/updateStorage") //new
    @ResponseBody
    fun updateStorage(@RequestParam(value = "data", defaultValue = "") data: String?): Msg {
        val jsonObj = JSONObject.parseObject(data)
        val id = jsonObj.getString("id")
        val AccessKey = jsonObj.getString("AccessKey")
        val AccessSecret = jsonObj.getString("AccessSecret")
        val Endpoint = jsonObj.getString("Endpoint")
        val Bucketname = jsonObj.getString("Bucketname")
        val RequestAddress = jsonObj.getString("RequestAddress")
        val storageType = jsonObj.getInteger("storageType")
        val keyname = jsonObj.getString("keyname")
        val storageKey = StorageKey()
        storageKey.setId(id)
        storageKey.setAccessKey(AccessKey)
        storageKey.setAccessSecret(AccessSecret)
        storageKey.setEndpoint(Endpoint)
        storageKey.setBucketName(Bucketname)
        storageKey.setRequestAddress(RequestAddress)
        storageKey.setStorageType(storageType)
        storageKey.setKeyName(keyname)
        return keysService.updateKey(storageKey)
    }

    @PostMapping("/getStorageById/{id}") //new
    @ResponseBody
    fun getselectkey(@PathVariable("id") id: String?): Msg {
        val msg = Msg()
        val storageKey: StorageKey = keysService.selectKeys(id)
        msg.data = storageKey
        return msg
    }

    @PostMapping("/getSettingConfig") //new
    @ResponseBody
    fun getSettingConfig(@RequestParam(value = "data", defaultValue = "") data: String?): Msg {
        val msg = Msg()
        val jsonObject = JSONObject()
        val subject = SecurityUtils.getSubject()
        val u = subject.principal as SysUser
        try {
            val uploadConfig: UploadConfig = uploadConfigService.getUpdateConfig()
            val config: Config = configService.getSourceType()
            val sysConfig: SysConfig = sysConfigService.getstate()
            uploadConfig.setUserStorage(java.lang.Long.valueOf(uploadConfig.getUserStorage()) / 1024 / 1024)
            uploadConfig.setVisitorStorage(java.lang.Long.toString(java.lang.Long.valueOf(uploadConfig.getVisitorStorage()) / 1024 / 1024))
            uploadConfig.setFileSizeTourists(java.lang.Long.toString(java.lang.Long.valueOf(uploadConfig.getFileSizeTourists()) / 1024 / 1024))
            uploadConfig.setFileSizeUser(java.lang.Long.toString(java.lang.Long.valueOf(uploadConfig.getFileSizeUser()) / 1024 / 1024))
            jsonObject["uploadConfig"] = uploadConfig
            jsonObject["config"] = config
            jsonObject["sysConfig"] = sysConfig
            msg.data = jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "操作失败"
        }
        return msg
    }

    /**
     * 更新配置
     *
     * @param configDto 配置dto
     * @return [Msg]
     */
    @PostMapping("/updateConfig") //new
    @ResponseBody
    fun updateConfig(@RequestBody configDto: ConfigDto): Msg {
        val msg = Msg()
        try {
            val uploadConfig: UploadConfig = configDto.getUploadConfig()
            val vm: String = uploadConfig.getVisitorStorage()
            if (vm.toLong() < -1 || vm.toLong() > 104857600 || uploadConfig.getFileSizeTourists()
                    .toLong() < 0 || uploadConfig.getFileSizeTourists()
                    .toLong() > 5120 || uploadConfig.getUserStorage() < 0 || uploadConfig.getUserStorage() > 1048576 || uploadConfig.getFileSizeUser()
                    .toLong() < 0 || uploadConfig.getFileSizeUser().toLong() > 5120
            ) {
                msg.info = "你输入的值不正确"
                msg.code = "500"
                return msg
            }
            val config: Config = configDto.getConfig()
            val sysConfig: SysConfig = configDto.getSysConfig()
            if (vm.toInt() == -1) {
                uploadConfig.setVisitorStorage("-1")
            } else {
                uploadConfig.setVisitorStorage(
                    java.lang.Long.toString(
                        uploadConfig.getVisitorStorage().toLong() * 1024 * 1024
                    )
                )
            }
            uploadConfig.setFileSizeTourists(
                java.lang.Long.toString(
                    uploadConfig.getFileSizeTourists().toLong() * 1024 * 1024
                )
            )
            uploadConfig.setUserStorage(uploadConfig.getUserStorage() * 1024 * 1024)
            uploadConfig.setFileSizeUser(java.lang.Long.toString(uploadConfig.getFileSizeUser().toLong() * 1024 * 1024))
            uploadConfigService.setUpdateConfig(uploadConfig)
            configService.setSourceType(config)
            sysConfigService.setstate(sysConfig)
            msg.info = "配置保存成功"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.info = "操作出现异常"
            msg.code = "500"
        }
        return msg
    }

    /**
     * 邮件配置
     *
     * @return [Msg]
     */
    @PostMapping(value = ["/getOrderConfig"]) //new
    @ResponseBody
    fun emailConfig(): Msg {
        val msg = Msg()
        var emailConfig: EmailConfig? = null
        var imgreview: Imgreview? = null
        try {
            val jsonObject = JSONObject()
            emailConfig = emailConfigService.getEmail()
            imgreview = imgreviewService.selectByPrimaryKey("1")
            jsonObject["emailConfig"] = emailConfig
            jsonObject["imgreview"] = imgreview
            msg.data = jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "获取相关配置信息失败"
        }
        return msg
    }

    @PostMapping("/updateEmailConfig") //new
    @ResponseBody
    fun updateemail(@RequestBody emailConfig: EmailConfig): Msg {
        val msg = Msg()
        try {
            if (null == emailConfig.getId() || null == emailConfig.getEmailName() || null == emailConfig.getEmailUrl() || null == emailConfig.getEmails() || null == emailConfig.getEmailKey() || null == emailConfig.getPort() || null == emailConfig.getEnable() || emailConfig.getEmailName()
                    .equals("") || emailConfig.getEmailUrl().equals("") || emailConfig.getEmails().equals("")
                || emailConfig.getEmailKey().equals("") || emailConfig.getPort().equals("")
            ) {
                msg.code = "110400"
                msg.info = "各参数不能为空"
                return msg
            }
            emailConfigService.updateEmail(emailConfig)
            msg.info = "保存成功"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "保存过程出现错误"
        }
        return msg
    }

    @PostMapping("/mailTest/{toMail}") //new
    @ResponseBody
    fun mailTest(@PathVariable("toMail") toMail: String?): Msg {
        var msg = Msg()
        val emailConfig: EmailConfig = emailConfigService.getEmail()
        if (null == emailConfig.getEmails() || null == emailConfig.getEmailKey() || null == emailConfig.getEmailUrl() || null == emailConfig.getPort() || null == emailConfig.getEmailName() || null == toMail) {
//        if(jsonObj.size()==0){
            msg.code = "110400"
            msg.info = "邮箱配置参数不能为空"
        } else {
            msg = sendTestEmail(emailConfig, toMail)
        }
        return msg
    }
}