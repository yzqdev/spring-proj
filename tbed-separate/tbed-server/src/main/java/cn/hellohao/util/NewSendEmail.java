package cn.hellohao.util;

import cn.hellohao.model.entity.Config;
import cn.hellohao.model.entity.EmailConfig;
import cn.hellohao.model.entity.Msg;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.server.HttpServerRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class NewSendEmail {

    public static MimeMessage emailMessage(EmailConfig emailConfig) {
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.put("mail.smtp.timeout", "20000");
        p.setProperty("mail.smtp.host", emailConfig.getEmailUrl());
        p.setProperty("mail.smtp.port", emailConfig.getPort());
        p.setProperty("mail.smtp.socketFactory.port", emailConfig.getPort());
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");

        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getEmails(), emailConfig.getEmailKey());
            }
        });
        session.setDebug(true);
        return new MimeMessage(session);
    }

    /**
     * 获取html中的内容
     */
    private static String build(String template,Map<String, Object> mapMsg) {
       try {
           //创建一个Configuration对象
           Configuration configuration = new Configuration(Configuration.getVersion());
           // 告诉config对象模板文件存放的路径。
           configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
           Console.log(configuration);
           // 设置config的默认字符集。一般是utf-8
           configuration.setDefaultEncoding("utf-8");
           //从config对象中获得模板对象。需要制定一个模板文件的名字。
           Template templateEngin = configuration.getTemplate(template);
           StringWriter stringWriter = new StringWriter();
            templateEngin.process(mapMsg, stringWriter);

           return  stringWriter.toString();
       }catch (IOException |TemplateException e){
           e.printStackTrace();
       }
        return null;
    }
    public static Integer sendEmail(EmailConfig emailConfig, String username, String uid, String toEmail, Config config) {
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
        String webname = config.getWebname();
        String domain = config.getDomain();
        try {
            //生成模板

            Map<String, Object> context = new HashMap<>();
            context.put("username", username);
            context.put("webname", webname);
            context.put("url", domain + "/user/activation?activation=" + uid + "&username=" + username);

            String output = build("emailTemplate/emailReg.html",context);
            MimeMessage message = emailMessage(emailConfig);
            message.setFrom(new InternetAddress(emailConfig.getEmails(), emailConfig.getEmailName(), "UTF-8"));
            // 收件人和抄送人

            message.setSubject(emailConfig.getEmailName() + "账号激活");
            message.setRecipients(Message.RecipientType.TO,toEmail);
            message.setContent(output, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport.send(message);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Msg sendTestEmail(EmailConfig emailConfig, String toEmail) {
        Msg msg = new Msg();
        MimeMessage message = emailMessage(emailConfig);
        try {
            message.setFrom(new InternetAddress(emailConfig.getEmails(), emailConfig.getEmailName(), "UTF-8"));
            // 收件人和抄送人
            String output = "<p>这是一条测试邮件，当您收到此邮件证明测试成功了</p>";
            message.setRecipients(Message.RecipientType.TO, toEmail);
            message.setSubject("Hellohao图像托管程序邮箱配置测试");
            message.setContent(output, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            String webname = "Hellohao图像托管程序";
            Transport.send(message);
            msg.setInfo("发送邮件指令已执行，请自行前往收信箱或垃圾箱查看是否收到测试邮件");

            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            msg.setCode("110500");
            msg.setInfo(e.getMessage());
            return msg;
        }
    }


    public static Integer sendEmailFindPass(EmailConfig emailConfig, String username, String uid, String toEmail, Config config) {
        MimeMessage message = emailMessage(emailConfig);
        String webname = config.getWebname();
        String domain = config.getDomain();
        String new_pass = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0, 10);
        try {
            //生成模板

            Map<String, Object> context = new HashMap<>();
            context.put("username", username);
            context.put("webname", webname);
            context.put("new_pass", new_pass);
            context.put("url", domain + "/user/retrieve?activation=" + uid + "&cip=" + HexUtil.encodeHexStr(new_pass, CharsetUtil.CHARSET_UTF_8));
            Writer writer = new StringWriter();
            String output =build("emailTemplate/emailFindPass.html",context);
            message.setFrom(new InternetAddress(emailConfig.getEmails(), emailConfig.getEmailName(), "UTF-8"));
            // 收件人和抄送人

            message.setRecipients(Message.RecipientType.TO, toEmail);
            message.setSubject("Hellohao图像托管程序邮箱配置测试");
            message.setContent(output, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport.send(message);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
