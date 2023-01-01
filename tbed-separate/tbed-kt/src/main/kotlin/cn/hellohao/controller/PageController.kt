package cn.hellohao.controller


/**
 * @author yanni
 * @date time 2022/5/3 23:21
 * @modified By:
 */
@Controller
@RequestMapping("/")
class PageController {
    @Resource
    var userService: UserService? = null

    @Value("\${webhost}")
    var webhost: String? = null
    @GetMapping("/home")
    fun indexPage(): String {
        return "home"
    }

    @GetMapping(value = ["/user/retrieve"]) //new
    fun retrieve(model: Model, activation: String?, cip: String?): String {
        val ret = 0
        try {
            val u2 = SysUser()
            u2.setUid(activation)
            val sysUser: SysUser = userService.getUsers(u2)
            sysUser.setIsok(1)
            val new_pass = HexUtil.decodeHexStr(cip) //解密密码
            sysUser.setPassword(Base64Encryption.encryptBASE64(new_pass.toByteArray()))
            val uid = UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault())
            sysUser.setUid(uid)
            if (sysUser != null) {
                val r: Int = userService.changeUser(sysUser)
                model.addAttribute("title", "成功")
                model.addAttribute("name", "新密码:$new_pass") //
                model.addAttribute("note", "密码已被系统重置，请即使登录修改你的新密码")
            } else {
                model.addAttribute("title", "抱歉")
                model.addAttribute("name", "为获取到用户信息")
                model.addAttribute("note", "操作失败")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            model.addAttribute("title", "抱歉")
            model.addAttribute("name", "系统操作过程中发生错误")
            model.addAttribute("note", "操作失败")
        }
        model.addAttribute("webhost", webhost)
        return "msg"
    }

    @GetMapping("/user/activation")
    fun activation(
        model: Model,
        @RequestParam("activation") activation: String?,
        @RequestParam("username") username: String
    ): String {
        val u2 = SysUser()
        u2.setUid(activation)
        val sysUser: SysUser = userService.getUsers(u2)
        model.addAttribute("webhost", webhost)
        return if (sysUser != null && sysUser.getIsok() === 0) {
            userService.getUserByUid(activation)
            model.addAttribute("title", "激活成功")
            model.addAttribute("name", "Hi~$username")
            model.addAttribute("note", "您的账号已成功激活看")
            "msg"
        } else {
            model.addAttribute("title", "操作无效")
            model.addAttribute("name", "该页面为无效页面")
            model.addAttribute("note", "请返回首页")
            "msg"
        }
    }
}