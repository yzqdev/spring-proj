package cn.hellohao.util

import cn.hellohao.model.entity.Config
import cn.hellohao.model.entity.EmailConfig
import cn.hellohao.model.entity.Msg
import cn.hutool.core.lang.Console
import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.HexUtil
import freemarker.template.Configuration
import freemarker.template.TemplateException
import java.io.File
import java.io.IOException
import java.io.StringWriter
import java.io.Writer
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object NewSendEmail {
    fun emailMessage(emailConfig: EmailConfig): MimeMessage {
        val p = Properties()
        p.setProperty("mail.smtp.auth", "true")
        p["mail.smtp.timeout"] = "20000"
        p.setProperty("mail.smtp.host", emailConfig.emailUrl)
        p.setProperty("mail.smtp.port", emailConfig.port)
        p.setProperty("mail.smtp.socketFactory.port", emailConfig.port)
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        //p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");
        val session = Session.getInstance(p, object : Authenticator() {
            // 设置认证账户信息
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(emailConfig.emails, emailConfig.emailKey)
            }
        })
        session.debug = true
        return MimeMessage(session)
    }

    /**
     * 获取html中的内容
     */
    private fun build(template: String, mapMsg: Map<String, Any>): String? {
        try {
            //创建一个Configuration对象
            val configuration = Configuration(Configuration.getVersion())
            // 告诉config对象模板文件存放的路径。
            configuration.setDirectoryForTemplateLoading(File("src/main/resources/templates"))
            Console.log(configuration)
            // 设置config的默认字符集。一般是utf-8
            configuration.defaultEncoding = "utf-8"
            //从config对象中获得模板对象。需要制定一个模板文件的名字。
            val templateEngin = configuration.getTemplate(template)
            val stringWriter = StringWriter()
            templateEngin.process(mapMsg, stringWriter)
            return stringWriter.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TemplateException) {
            e.printStackTrace()
        }
        return null
    }

    @kotlin.jvm.JvmStatic
    fun sendEmail(emailConfig: EmailConfig, username: String, uid: String, toEmail: String?, config: Config): Int {
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.debug", "false");
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.timeout", "20000");
//        props.put("mail.smtp.port", emailConfig.getPort());//465  25
//        props.put("mail.smtp.host", emailConfig.getEmailUrl());
//        // 配置一次即可，可以配置为静态方法
////        OhMyEmail.config(OhMyEmail.SMTP_QQ(false), "xxxx@qq.com", "your@password");
//        Session session = Session.getInstance(props, new Authenticator() {
//            // 设置认证账户信息
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(emailConfig.getEmails(), emailConfig.getEmailKey());
//            }
//        });
//        session.setDebug(true);
        val webname = config.webname
        val domain = config.domain
        return try {
            //生成模板
            val context: MutableMap<String, Any> = HashMap()
            context["username"] = username
            context["webname"] = webname
            context["url"] = "$domain/user/activation?activation=$uid&username=$username"
            val output = build("emailTemplate/emailReg.html", context)
            val message = emailMessage(emailConfig)
            message.setFrom(InternetAddress(emailConfig.emails, emailConfig.emailName, "UTF-8"))
            // 收件人和抄送人
            message.subject = emailConfig.emailName + "账号激活"
            message.setRecipients(Message.RecipientType.TO, toEmail)
            message.setContent(output, "text/html;charset=UTF-8")
            message.sentDate = Date()
            message.saveChanges()
            Transport.send(message)
            1
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    @kotlin.jvm.JvmStatic
    fun sendTestEmail(emailConfig: EmailConfig, toEmail: String?): Msg {
        val msg = Msg()
        val message = emailMessage(emailConfig)
        return try {
            message.setFrom(InternetAddress(emailConfig.emails, emailConfig.emailName, "UTF-8"))
            // 收件人和抄送人
            val output = "<p>这是一条测试邮件，当您收到此邮件证明测试成功了</p>"
            message.setRecipients(Message.RecipientType.TO, toEmail)
            message.subject = "Hellohao图像托管程序邮箱配置测试"
            message.setContent(output, "text/html;charset=UTF-8")
            message.sentDate = Date()
            message.saveChanges()
            val webname = "Hellohao图像托管程序"
            Transport.send(message)
            msg.info = "发送邮件指令已执行，请自行前往收信箱或垃圾箱查看是否收到测试邮件"
            msg
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            msg.code = "110500"
            msg.info = e.message.toString()
            msg
        }
    }

    @kotlin.jvm.JvmStatic
    fun sendEmailFindPass(
        emailConfig: EmailConfig,
        username: String,
        uid: String,
        toEmail: String?,
        config: Config
    ): Int {
        val message = emailMessage(emailConfig)
        val webname = config.webname
        val domain = config.domain
        val new_pass = UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault()).substring(0, 10)
        return try {
            //生成模板
            val context: MutableMap<String, Any> = HashMap()
            context["username"] = username
            context["webname"] = webname
            context["new_pass"] = new_pass
            context["url"] = domain + "/user/retrieve?activation=" + uid + "&cip=" + HexUtil.encodeHexStr(
                new_pass,
                CharsetUtil.CHARSET_UTF_8
            )
            val writer: Writer = StringWriter()
            val output = build("emailTemplate/emailFindPass.html", context)
            message.setFrom(InternetAddress(emailConfig.emails, emailConfig.emailName, "UTF-8"))
            // 收件人和抄送人
            message.setRecipients(Message.RecipientType.TO, toEmail)
            message.subject = "Hellohao图像托管程序邮箱配置测试"
            message.setContent(output, "text/html;charset=UTF-8")
            message.sentDate = Date()
            message.saveChanges()
            Transport.send(message)
            1
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}