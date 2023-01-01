package cn.hellohao.controller

import cn.hellohao.auth.token.JWTUtil
import cn.hellohao.config.SysName
import cn.hellohao.model.dto.UserFind
import cn.hellohao.model.dto.UserLoginDto
import cn.hellohao.model.entity.*
import cn.hellohao.service.*
import cn.hellohao.serviceimport.UserService
import cn.hellohao.util.Base64Encryption
import cn.hellohao.util.GetIPS.Companion.getIpAddr
import cn.hellohao.util.NewSendEmail.sendEmail
import cn.hellohao.util.NewSendEmail.sendEmailFindPass
import cn.hellohao.util.Print
import cn.hellohao.util.SetText.checkEmail
import cn.hellohao.utilimport.RequestHelper
import cn.hutool.core.lang.Console
import cn.hutool.log.StaticLog
import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.UsernamePasswordToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/user")
class UserController(private val userService: UserService, private val emailConfigService: EmailConfigService, private val configService: ConfigService, private val uploadConfigService: UploadConfigService, private val sysConfigService: SysConfigService, var iRedisService: IRedisService) {


    @Value("\${webhost}")
    var webhost: String? = null


    @PostMapping("/register")
    @ResponseBody
    fun Register(@RequestBody userLoginDto: UserLoginDto): Msg { //Validated
        val msg = Msg()
        val request: HttpServletRequest = RequestHelper.request
        val username: String? = userLoginDto.username
        val email: String? = userLoginDto.email
        val password: String = Base64Encryption.encryptBASE64(userLoginDto.password.getBytes())
        val userIP = getIpAddr(request)
        val verifyCodeForRegister: String? = userLoginDto.verifyCode
        val redis_verifyCodeForRegister: Any = iRedisService.getValue(userIP + "_hellohao_verifyCodeForRegister")
        if (!checkEmail(email)) {
            msg.code = "110403"
            msg.info = "邮箱格式不正确"
            return msg
        }
        val regex = "^\\w+$"
        if (username.length > 20 || !username?.matches(regex)!!) {
            msg.code = "110403"
            msg.info = "用户名不得超过20位字符"
            return msg
        }
        if (null == redis_verifyCodeForRegister) {
            msg.code = "4035"
            msg.info = "验证码已失效，请重新弄获取。"
            return msg
        } else if (null == verifyCodeForRegister) {
            msg.code = "4036"
            msg.info = "验证码不能为空。"
            return msg
        }
        if (redis_verifyCodeForRegister.toString().lowercase(Locale.getDefault())
                .compareTo(verifyCodeForRegister.lowercase(Locale.getDefault())) == 0
        ) {
            val sysUser = SysUser()
            val updateConfig: UploadConfig = uploadConfigService.updateConfig()
            val emailConfig: EmailConfig =
                emailConfigService.getOne(KtQueryWrapper(EmailConfig()).eq(EmailConfig::id, "1"))
            val countusername: Int = userService.countusername(username)
            val countmail: Int = userService.countmail(email)
            val sysConfig: SysConfig = sysConfigService.getstate()
            if (sysConfig.register !== 1) {
                msg.code = "110403"
                msg.info = "本站已暂时关闭用户注册功能"
                return msg
            }
            if (countusername == 1 || !SysName.CheckSysName(username)) {
                msg.code = "110406"
                msg.info = "此用户名已存在"
                return msg
            }
            if (countmail == 1) {
                msg.code = "110407"
                msg.info = "此邮箱已被注册"
                return msg
            }
            val uid = UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault())
            sysUser.apply {
                level=1
                this.uid=uid
                birthday= LocalDateTime.now()
                memory=updateConfig.userStorage
                groupId="1"
                this.email=email
                this.password=password
                createTime= LocalDateTime.now()
                updateTime= LocalDateTime.now()
            }

            val config: Config = configService.getSourceType()
            val type = 0
            Console.log("email=>{}", emailConfig)
            if (emailConfig != null && emailConfig.enable) {
                sysUser.isok=0
                val thread =
                    Thread { val a = sendEmail(emailConfig, sysUser.username , uid, sysUser.email, config) }
                thread.start()
                msg.info = "注册成功,请注意查收邮箱尽快激活账户"
            } else {
                sysUser.isok=1
                msg.info = "注册成功,快去登陆吧"
            }
            userService.register(sysUser)
        } else {
            msg.code = "110408"
            msg.info = "验证码不正确" //失效也要处理。
        }
        return msg
    }

    @PostMapping("/login")
    @ResponseBody
    fun login(@RequestBody userLoginDto: UserLoginDto): Msg {
        val msg = Msg()
        val email: String = userLoginDto.email
        val password: String = Base64Encryption.encryptBASE64(userLoginDto.getPassword().getBytes())
        val verifyCode: String = userLoginDto.verifyCode
        val userIP = getIpAddr(RequestHelper.request)
        val redis_VerifyCode: Any = iRedisService.getValue(userIP + "_hellohao_verifyCode")
        if (null == redis_VerifyCode) {
            msg.code = "4035"
            msg.info = "验证码已失效，请重新弄获取。"
            return msg
        } else if (null == verifyCode) {
            msg.code = "4036"
            msg.info = "验证码不能为空。"
            return msg
        }
        if (redis_VerifyCode.toString().lowercase(Locale.getDefault())
                .compareTo(verifyCode.lowercase(Locale.getDefault())) == 0
        ) {
            val subject = SecurityUtils.getSubject()
            val tokenOBJ = UsernamePasswordToken(email, password)
            tokenOBJ.isRememberMe = true
            return try {
                subject.login(tokenOBJ)
                //一小时
                SecurityUtils.getSubject().session.timeout = 3600000
                val jsonObject = JSONObject()
                val sysUser = SecurityUtils.getSubject().principal as SysUser
                if (sysUser.isok === 0) {
                    msg.info = "你的账号暂未激活"
                    msg.code = "110403"
                    return msg
                }
                if (sysUser.isok!! < 0) {
                    msg.info = "你的账户已被冻结"
                    msg.code = "110403"
                    return msg
                }
                StaticLog.warn(sysUser.toString())
                val token = JWTUtil.createToken(sysUser)
                val u = JWTUtil.checkToken(token)
                Console.error(u)
                val su = SecurityUtils.getSubject()
                println("当前用户角色：admin:" + su.hasRole("admin"))
                msg.info = "登录成功"
                jsonObject["token"] = token
                jsonObject["RoleLevel"] = if (sysUser.level === 2) "admin" else "sysUser"
                jsonObject["userName"] = sysUser.username
                msg.data = jsonObject
                msg
            } catch (e: UnknownAccountException) {
                //此异常说明用户名不存在
                msg.code = "4000"
                msg.info = "登录邮箱不存在"
                System.err.println("邮箱不存在")
                e.printStackTrace()
                msg
            } catch (e: IncorrectCredentialsException) {
                msg.code = "4000"
                msg.info = "登录密码错误"
                System.err.println("密码不存在")
                e.printStackTrace()
                msg
            } catch (e: Exception) {
                msg.code = "5000"
                msg.info = "登录失败"
                System.err.println("登录失败")
                e.printStackTrace()
                msg
            }
        } else {
            msg.code = "40034"
            msg.info = "验证码不正确" //失效也要处理。
        }
        return msg
    }

    @PostMapping(value = ["/logout"]) //new
    @ResponseBody
    fun exit(model: Model?, request: HttpServletRequest?, response: HttpServletResponse?, session: HttpSession?): Msg {
        val msg = Msg()
        val subject = SecurityUtils.getSubject()
        subject.logout()
        msg.info = "退出成功"
        Print.Normal("用户账号退出成功")
        return msg
    }

    @PostMapping("/retrievePass") //new
    @ResponseBody
    fun retrievePass(request: HttpServletRequest?, @RequestBody userFind: UserFind): Msg {
        val msg = Msg()
        try {
            val email = userFind.email
            val retrieveCode = userFind.retrieveCode
            val userIP = getIpAddr(request)
            val redis_verifyCodeForEmailRetrieve: Any =
                iRedisService.getValue(userIP + "_hellohao_verifyCodeForEmailRetrieve")
            val emailConfig: EmailConfig = emailConfigService.getEmail()
            if (null == redis_verifyCodeForEmailRetrieve) {
                msg.code = "4035"
                msg.info = "验证码已失效，请重新弄获取。"
                return msg
            } else if (null == retrieveCode) {
                msg.code = "4036"
                msg.info = "验证码不能为空。"
                return msg
            }
            if (redis_verifyCodeForEmailRetrieve.toString().lowercase(Locale.getDefault())
                    .compareTo(retrieveCode.lowercase(Locale.getDefault())) != 0
            ) {
                msg.code = "40034"
                msg.info = "验证码不正确"
                return msg
            }
            val ret: Int = userService.countmail(email)
            if (ret > 0) {
                if (emailConfig.enable == true) {
                    val u2 = SysUser()
                    u2.email=email
                    val sysUser: SysUser = userService.getUsers(u2)
                    if (sysUser.isok === -1) {
                        msg.code = "110110"
                        msg.info = "当前用户已被冻结，禁止操作"
                        return msg
                    }
                    val config: Config = configService.getSourceType()
                    val thread = Thread {
                        val a = sendEmailFindPass(
                            emailConfig,
                            sysUser.username,
                            sysUser.uid,
                            sysUser.email,
                            config
                        ) //SendEmail.sendEmailT(message, sysUser.username(), sysUser.getUid(), sysUser.email,emailConfig,config);
                    }
                    thread.start()
                    msg.info =
                        "重置密码的验证链接已发送至该邮箱，请前往邮箱验证并重置密码。【若长时间未收到邮件，请检查垃圾箱】"
                } else {
                    msg.code = "400"
                    msg.info = "本站暂未开启邮箱服务，请联系管理员"
                }
            } else {
                msg.code = "110404"
                msg.info = "未找到邮箱所在的用户"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "系统发生错误"
        }
        return msg
    }
}