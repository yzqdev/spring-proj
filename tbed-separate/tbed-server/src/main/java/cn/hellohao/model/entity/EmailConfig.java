package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件配置
 *
 * @author yanni
 * @date 2021/11/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("email_config")
public class EmailConfig {
    private String id ;
    private String emails;
    /**
     * 电子邮件的关键
     */
    private String emailKey;
    /**
     * 电子邮件网址
     */
    private String emailUrl;
    /**
     * 端口
     */
    private String port;
    private String emailName;
    private Boolean enable;

}
