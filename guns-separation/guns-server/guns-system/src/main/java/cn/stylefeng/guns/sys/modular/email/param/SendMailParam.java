package cn.stylefeng.guns.sys.modular.email.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yanni
 * @date time 2021/11/26 21:53
 * @modified By:
 */
@Data
public class SendMailParam  implements Serializable {

    /**
     * 邮件接收方，可多人
     */
    private String[] tos;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 标题
     */
    private String title;
    /**
     * 邮件内容
     */
    private String content;
}
