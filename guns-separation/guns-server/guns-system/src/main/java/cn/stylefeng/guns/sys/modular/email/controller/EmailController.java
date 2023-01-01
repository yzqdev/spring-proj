/*
Copyright [2020] [https://www.stylefeng.cn]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Guns源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns-separation
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns-separation
6.若您的项目无法满足以上几点，可申请商业授权，获取Guns商业授权许可，请在官网购买授权，地址为 https://www.stylefeng.cn
 */
package cn.stylefeng.guns.sys.modular.email.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.mail.MailException;
import cn.hutool.log.Log;
import cn.stylefeng.guns.core.annotion.BusinessLog;
import cn.stylefeng.guns.core.context.requestno.RequestNoContext;
import cn.stylefeng.guns.core.enums.LogAnnotionOpTypeEnum;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.core.pojo.response.ResponseData;
import cn.stylefeng.guns.core.pojo.response.SuccessResponseData;
import cn.stylefeng.guns.sys.modular.email.enums.SysEmailExceptionEnum;

import cn.stylefeng.guns.sys.modular.email.param.SendMailParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * 邮件发送控制器
 *
 * @author stylefeng
 * @date 2020/6/9 23:02
 */
@RestController
public class EmailController {

    private static final Log log = Log.get();
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 发送邮件
     *
     * @author stylefeng, xuyuxiang
     * @date 2020/6/9 23:02
     */
    @PostMapping("/email/sendEmail")
    @BusinessLog(title = "发送邮件", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData sendEmail(@RequestBody SendMailParam sendMailParam) {


        String[] to = sendMailParam.getTos();
        if (ObjectUtil.isEmpty(to)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TO_EMPTY);
        }

        String title = sendMailParam.getTitle();
        if (ObjectUtil.isEmpty(title)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TITLE_EMPTY);
        }

        String content = sendMailParam.getContent();
        if (ObjectUtil.isEmpty(content)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_CONTENT_EMPTY);
        }
        try {


            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("主题：验证邮件！");
            message.setText(content);
            javaMailSender.send(message);

            new SuccessResponseData("发送成功!");

        } catch (MailException e) {
            log.error(">>> 邮件发送异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_SEND_ERROR);
        }
        return new SuccessResponseData();
    }

    @GetMapping("/test/from")
    public ResponseData getFrom() {
        return ResponseData.success(from);
    }

    /**
     * 发送邮件(html)
     *
     * @author stylefeng, xuyuxiang
     * @date 2020/6/9 23:02
     */
    @PostMapping("/email/sendEmailHtml")
    @BusinessLog(title = "发送邮件", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData sendEmailHtml(@RequestBody SendMailParam sendMailParam) {
        String[] to = sendMailParam.getTos();
        if (ObjectUtil.isEmpty(to)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TO_EMPTY);
        }

        String title = sendMailParam.getTitle();
        if (ObjectUtil.isEmpty(title)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TITLE_EMPTY);
        }

        String content = sendMailParam.getContent();
        if (ObjectUtil.isEmpty(content)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_CONTENT_EMPTY);
        }
        try {
            //创建一个MINE消息
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            //谁发
            minehelper.setFrom(from);
            //谁要接收
            minehelper.setTo(to);
            //邮件主题
            minehelper.setSubject(title);
            //邮件内容   true 表示带有附件或html
            minehelper.setText(content, true);

            javaMailSender.send(message);
            return ResponseData.success("HTML邮件成功");

        } catch (MailException | MessagingException e) {
            log.error(">>> 邮件发送异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_SEND_ERROR);
        }
    }
}
