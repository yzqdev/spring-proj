package cn.hellohao.controller

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-17 14:22
 */
@Controller
@RequestMapping("/admin")
@Tag(name = "管理员")
class AdminController {
    @Resource
    private val imgService: ImgService? = null

    @Resource
    private val keysService: KeysService? = null

    @Resource
    private val userService: UserService? = null

    @Resource
    private val imgreviewService: ImgreviewService? = null

    @Resource
    private val imgTempService: ImgTempService? = null

    @Resource
    private val uploadConfigService: UploadConfigService? = null

    @Resource
    private val codeService: CodeService? = null

    @Resource
    private val imgAndAlbumService: ImgAndAlbumService? = null

    @Resource
    private val albumService: AlbumService? = null

    @Resource
    private val nosImageupload: NOSImageupload? = null

    @Resource
    private val ossImageupload: OSSImageupload? = null

    @Resource
    private val cosImageupload: COSImageupload? = null

    @Resource
    private val kodoImageupload: KODOImageupload? = null

    @Resource
    private val ussImageupload: USSImageupload? = null

    @Resource
    private val uFileImageupload: UFileImageupload? = null

    @Resource
    private val ftpImageupload: FTPImageupload? = null

    /**
     * 概述数据
     *
     * @return [Msg]
     */
    @PostMapping(value = ["/overviewData"]) //new
    @ResponseBody
    fun overviewData(): Msg {
        val msg = Msg()
        val subject = SecurityUtils.getSubject()
        var sysUser = subject.principal as SysUser
        sysUser = userService.getUsers(sysUser)
        val jsonObject = JSONObject()
        val uploadConfig: UploadConfig = uploadConfigService.getUpdateConfig()
        //查询非法个数
        val imgreview: Imgreview = imgreviewService.selectByPrimaryKey("1")
        //查询有没有启动鉴别功能
        val isImgreviewOK: Imgreview = imgreviewService.selectByusing(1)
        var ok = "false"
        jsonObject["myImgTotal"] = imgService.countimg(sysUser.getId()) //我的图片数
        jsonObject["myAlbumTitle"] = albumService.selectAlbumCount(sysUser.getId()) //我的画廊数量
        val memory: Long = sysUser.getMemory() //分配量
        val usermemory =
            if (imgService.getUserMemory(sysUser.getId()) == null) 0L else imgService.getUserMemory(sysUser.getId())
        if (memory == 0L) {
            jsonObject["myMemory"] = "无容量"
        } else {
            val aDouble = String.format("%.2f", usermemory.toDouble() / memory.toDouble() * 100).toDouble()
            if (aDouble >= 999) {
                jsonObject["myMemory"] = 999
            } else {
                jsonObject["myMemory"] = aDouble
            }
        }
        jsonObject["myMemorySum"] = readableFileSize(memory)
        if (sysUser.getLevel() > 1) {
            ok = "true"
            //管理员
            jsonObject["imgTotal"] = imgService.counts(null) //admin  站点图片数
            jsonObject["userTotal"] = userService.getUserTotal() //admin  用户个数
            jsonObject["ViolationImgTotal"] = imgreview.getCount() //admin 非法图片
            jsonObject["ViolationSwitch"] = if (isImgreviewOK == null) 0 else isImgreviewOK.getId() //admin 非法图片开关
            jsonObject["VisitorUpload"] = uploadConfig.getIsUpdate() //是否禁用了游客上传
            jsonObject["VisitorMemory"] = readableFileSize(uploadConfig.getVisitorStorage().toLong()) //访客共大小
            if (uploadConfig.getIsUpdate() !== 1) {
                jsonObject["VisitorUpload"] = 0 //是否禁用了游客上传
                jsonObject["VisitorProportion"] = 100.00 //游客用量%占比
                jsonObject["VisitorMemory"] = "禁用" //访客共大小
            } else {
                val temp: Long = if (imgService.getUserMemory("0") == null) 0 else imgService.getUserMemory("0")
                jsonObject["UsedMemory"] = if (temp == null) 0 else readableFileSize(temp) //访客已用大小
                if (java.lang.Long.valueOf(uploadConfig.getVisitorStorage()) == 0L) {
                    jsonObject["VisitorProportion"] = 100.00 //游客用量%占比
                } else if (java.lang.Long.valueOf(uploadConfig.getVisitorStorage()) == -1L) {
                    jsonObject["VisitorProportion"] = 0 //游客用量%占比
                    jsonObject["VisitorMemory"] = "无限" //访客共大小
                } else {
                    val sum: Double = java.lang.Double.valueOf(uploadConfig.getVisitorStorage())
                    val aDouble = java.lang.Double.valueOf(String.format("%.2f", temp.toDouble() / sum * 100))
                    if (aDouble >= 999) {
                        jsonObject["VisitorProportion"] = 999 //游客用量%占比
                    } else {
                        jsonObject["VisitorProportion"] = aDouble //游客用量%占比
                    }
                }
            }
        }
        jsonObject["ok"] = ok
        msg.data = jsonObject
        return msg
    }

    @PostMapping(value = ["/SpaceExpansion/{code}"]) //new
    @ResponseBody
    fun SpaceExpansion(@PathVariable("code") codeStr: String?): Msg {
        val msg = Msg()
        val subject = SecurityUtils.getSubject()
        var sysUser = subject.principal as SysUser
        sysUser = userService.getUsers(sysUser)
        if (sysUser.getIsok() === 0) {
            msg.code = "100403"
            msg.info = "你暂时无法使用此功能"
            return msg
        }
        return if (null == sysUser) {
            msg.code = "100405"
            msg.info = "用户信息不存在"
            msg
        } else {
            var sizes: Long = 0
            val code: Code = codeService.selectCodeKey(codeStr)
            if (null == code) {
                msg.code = "100404"
                msg.info = "扩容码不存在,请重新填写"
                return msg
            }
            val userMemory: Long = java.lang.Long.valueOf(sysUser.getMemory())
            sizes = java.lang.Long.valueOf(code.getValue()) + userMemory
            val newMemorySysUser = SysUser()
            newMemorySysUser.setMemory(sizes)
            newMemorySysUser.setId(sysUser.getId())
            userService.usersetmemory(newMemorySysUser, codeStr)
            msg.info = "你已成功扩容" + readableFileSize(sizes)
            msg
        }
    }

    //new
    @get:ResponseBody
    @get:PostMapping("/getRecently")
    val recently: Msg

    //new get() {
    @get:ResponseBody
    val msg = Msg()
    val jsonObject = JSONObject()
    try
    {
        val subject = SecurityUtils.getSubject()
        var sysUser = subject.principal as SysUser
        sysUser = userService.getUsers(sysUser)
        if (sysUser.getLevel() > 1) {
            jsonObject["RecentlyUser"] = imgService.recentlyUser()
            jsonObject["RecentlyUploaded"] = imgService.recentlyUploaded(sysUser.getId())
        } else {
            jsonObject["RecentlyUploaded"] = imgService.recentlyUploaded(sysUser.getId())
        }
    }catch ( e: java.lang.Exception)
    {
        e.printStackTrace()
        msg.info = "系统内部错误"
        msg.code = "500"
        return msg
    }
    msg.data = jsonObject
    return msg
}

//new
@get:PostMapping("/getYyyy")
val yyyy: Msg
    @PostMapping("/getYyyy") //new
    @ResponseBody get() {
        val msg = Msg()
        val subject = SecurityUtils.getSubject()
        val u = subject.principal as SysUser
        val jsonObject = JSONObject()
        jsonObject["allYyyy"] = imgService.getyyyy(null)
        jsonObject["userYyyy"] = imgService.getyyyy(u.getId())
        msg.data = jsonObject
        return msg
    }

@PostMapping("/getChart") //new
@ResponseBody
fun getChart(@RequestBody data: JSONObject): Msg {
    val msg = Msg()
    val jsonObject = JSONObject.parseObject(data.toString())
    val yyyy = jsonObject.getString("year")
    val type = jsonObject.getInteger("type")
    val subject = SecurityUtils.getSubject()
    val u = subject.principal as SysUser
    var list: List<ImageVo>? = null
    list = if (u.getLevel() > 1) {
        if (type == 2) {
            val homeImgDto = HomeImgDto()
            homeImgDto.setYear(yyyy)
            //images.setYyyy(yyyy);
            imgService.countByM(homeImgDto)
        } else {
            val homeImgDto = HomeImgDto()
            homeImgDto.setYear(yyyy)
            homeImgDto.setUserId(u.getId())
            imgService.countByM(homeImgDto)
        }
    } else {
        val homeImgDto = HomeImgDto()
        homeImgDto.setYear(yyyy)
        homeImgDto.setUserId(u.getId())
        imgService.countByM(homeImgDto)
    }
    val json =
        JSONArray.parseArray("[{\"id\":1,\"monthNum\":\"一月\",\"countNum\":0},{\"id\":2,\"monthNum\":\"二月\",\"countNum\":0},{\"id\":3,\"monthNum\":\"三月\",\"countNum\":0},{\"id\":4,\"monthNum\":\"四月\",\"countNum\":0},{\"id\":5,\"monthNum\":\"五月\",\"countNum\":0},{\"id\":6,\"monthNum\":\"六月\",\"countNum\":0},{\"id\":7,\"monthNum\":\"七月\",\"countNum\":0},{\"id\":8,\"monthNum\":\"八月\",\"countNum\":0},{\"id\":9,\"monthNum\":\"九月\",\"countNum\":0},{\"id\":10,\"monthNum\":\"十月\",\"countNum\":0},{\"id\":11,\"monthNum\":\"十一月\",\"countNum\":0},{\"id\":12,\"monthNum\":\"十二月\",\"countNum\":0}]")
    println(list)
    println("这是啥")
    val jsonArray = JSONArray()
    for (imageVo in list) {
        for (i in json.indices) {
            val jobj = json.getJSONObject(i)
            if (jobj.getInteger("id") == imageVo.getMonthNum()) {
                jobj["monthNum"] = AdminController.Companion.getChinaes(imageVo.getMonthNum())
                jobj["countNum"] = imageVo.getCountNum()
            }
        }
    }
    msg.data = json
    return msg
}

//new
val storage: Msg
    @PostMapping("/getStorage") //new
    @ResponseBody get() {
        val msg = Msg()
        val storage: List<StorageKey> = keysService.getStorage()
        msg.data = storage
        return msg
    }

//new
val storageName: Msg
    @PostMapping("/getStorageName") //new
    @ResponseBody get() {
        val msg = Msg()
        val storage: List<StorageKey> = keysService.getStorageName()
        msg.data = storage
        return msg
    }

/**
 * 选择照片
 *
 * @param imgSearchDto img搜索dto
 * @return [Msg]
 */
@PostMapping(value = ["/selectPhoto"])
@ResponseBody
fun selectPhoto(@RequestBody imgSearchDto: ImgSearchDto): Msg {
    val msg = Msg()
    val subject = SecurityUtils.getSubject()
    val sysUser = subject.principal as SysUser
    if (imgSearchDto.getStartTime() != null) {
        try {
            val date1 =
                LocalDateTime.parse(imgSearchDto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val date2 = if (imgSearchDto.getStopTime() == null) LocalDateTime.now() else LocalDateTime.parse(
                imgSearchDto.getStopTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val compareTo = date1.compareTo(date2)
            if (compareTo == 1) {
                msg.code = "110500"
                msg.info = "起始日期不能大于结束日期"
                return msg
            }
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "您输入的日期不正确"
            return msg
        }
    }
    if (imgSearchDto.getViolation()) {
        imgSearchDto.setViolation(true)
    }

    //if(img.getClassifulids()!=null){
    //    String[] calssif = img.getClassifulids().split(",");
    //    img.setClassifulids(calssif);
    //}
    if (subject.hasRole("admin")) {
        imgSearchDto.setUserId(null)
    } else {
        //普通用户
        imgSearchDto.setUserId(sysUser.getId())
    }
    imgSearchDto.setPageNum(1)
    imgSearchDto.setPageSize(10)
    val imgList: List<Images> = imgService.selectImages(imgSearchDto)
    val images = Page<Images>(1, 10)
    images.setRecords(imgList)
    val pageResultBean = PageResultBean(images.total, images.records)
    msg.data = pageResultBean
    return msg
}

//new
val userInfo: Msg
    @PostMapping(value = ["/getUserInfo"]) //new
    @ResponseBody get() {
        val msg = Msg()
        try {
            val subject = SecurityUtils.getSubject()
            val sysUser = subject.principal as SysUser
            val u = SysUser()
            u.setId(sysUser.getId())
            val sysUserInfo: SysUser = userService.getUsers(u)
            val jsonObject = JSONObject()
            jsonObject["username"] = sysUserInfo.getUsername()
            jsonObject["email"] = sysUserInfo.getEmail()
            msg.data = jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "操作失败"
        }
        return msg
    }

@PostMapping("/setUserInfo") //new
@ResponseBody
fun setUserInfo(@RequestBody jsonObject: Map<String?, String?>): Msg {
    val msg = Msg()
    try {
        val username = jsonObject["username"]
        val email = jsonObject["email"]
        val password = jsonObject["password"]
        val subject = SecurityUtils.getSubject()
        val u = subject.principal as SysUser
        val sysUser = SysUser()
        if (!checkEmail(email)) {
            msg.code = "110403"
            msg.info = "邮箱格式不正确"
            return msg
        }
        val regex = "^\\w+$"
        if (username!!.length > 20 || !username.matches(regex)) {
            msg.code = "110403"
            msg.info = "用户名不得超过20位字符"
            return msg
        }
        if (subject.hasRole("admin")) {
            val sysUserOld = SysUser()
            sysUserOld.setId(u.getId())
            val sysUserInfo: SysUser = userService.getUsers(sysUserOld)
            if (!sysUserInfo.getUsername().equals(username)) {
                val countusername: Int = userService.countusername(username)
                if (countusername == 1 || !SysName.CheckSysName(username)) {
                    msg.code = "110406"
                    msg.info = "此用户名已存在"
                    return msg
                } else {
                    sysUser.setUsername(username)
                }
            }
            if (!sysUserInfo.getEmail().equals(email)) {
                val countmail: Int = userService.countmail(email)
                if (countmail == 1) {
                    msg.code = "110407"
                    msg.info = "此邮箱已被注册"
                    return msg
                } else {
                    sysUser.setEmail(email)
                }
            }
            sysUser.setPassword(Base64Encryption.encryptBASE64(password!!.toByteArray()))
            sysUser.setUid(u.getUid())
        } else {
            sysUser.setPassword(Base64Encryption.encryptBASE64(password!!.toByteArray()))
            sysUser.setUid(u.getUid())
        }
        sysUser.setUpdateTime(LocalDateTime.now())
        userService.change(sysUser)
        msg.info = "信息修改成功，请重新登录"
    } catch (e: Exception) {
        e.printStackTrace()
        msg.code = "110500"
        msg.info = "服务执行异常，请稍后再试"
    }
    return msg
}

@PostMapping("/deleImages") //new
@ResponseBody
fun deleImages(@RequestBody images: Array<String?>): Msg {
    val msg = Msg()
    val subject = SecurityUtils.getSubject()
    val sysUser = subject.principal as SysUser
    if (null == sysUser) {
        msg.code = "500"
        msg.info = "当前用户信息不存在"
        return msg
    }
    if (images.size == 0) {
        msg.code = "404"
        msg.info = "为获取到图像信息"
        return msg
    }
    for (i in images.indices) {
        val imgid = images[i]
        val image: Images = imgService.selectByPrimaryKey(imgid)
        val keyID: String = image.getSource()
        val imgname: String = image.getImgName()
        val key: StorageKey = keysService.selectKeys(keyID)
        if (!subject.hasRole("admin")) {
            if (!image.getUserId().equals(sysUser.getId())) {
                break
            }
        }
        var isDele = false
        try {
            if (key.getStorageType() === 1) {
                isDele = nosImageupload.delNOS(key.getId(), imgname)
            } else if (key.getStorageType() === 2) {
                isDele = ossImageupload.delOSS(key.getId(), imgname)
            } else if (key.getStorageType() === 3) {
                isDele = ussImageupload.delUSS(key.getId(), imgname)
            } else if (key.getStorageType() === 4) {
                isDele = kodoImageupload.delKODO(key.getId(), imgname)
            } else if (key.getStorageType() === 5) {
                isDele = deleteLOCImg(imgname)
            } else if (key.getStorageType() === 6) {
                isDele = cosImageupload.delCOS(key.getId(), imgname)
            } else if (key.getStorageType() === 7) {
                isDele = ftpImageupload.delFTP(key.getId(), imgname)
            } else if (key.getStorageType() === 8) {
                isDele = uFileImageupload.delUFile(key.getId(), imgname)
            } else {
                System.err.println("未获取到对象存储参数，删除失败。")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (isDele) {
            try {
                imgTempService.delImgAndExp(image.getImgUid())
                imgService.deleteImgById(imgid)
                imgAndAlbumService.deleteImgAndAlbum(imgname)
            } catch (e: Exception) {
                e.printStackTrace()
                msg.info = "图片记录删除失败，请重试"
                msg.code = "500"
                return msg
            }
            msg.info = "删除成功"
        } else {
            imgTempService.delImgAndExp(image.getImgUid())
            imgService.deleteImgById(imgid)
            imgAndAlbumService.deleteImgAndAlbum(imgname)
            msg.info = "图片记录已删除，但是图片源删除失败"
        }
    }
    return msg
}

companion object {
    //工具函数
    private fun getChinaes(v: Int): String {
        var ch = ""
        ch = when (v) {
            1 -> "一月"
            2 -> "二月"
            3 -> "三月"
            4 -> "四月"
            5 -> "五月"
            6 -> "六月"
            7 -> "七月"
            8 -> "八月"
            9 -> "九月"
            10 -> "十月"
            11 -> "十一月"
            12 -> "十二月"
            else -> "" //可选
        }
        return ch
    }
}
}