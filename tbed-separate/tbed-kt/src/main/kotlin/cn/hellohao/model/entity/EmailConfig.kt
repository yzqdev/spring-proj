package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

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
class EmailConfig {
    var  id: String? = null
    var  emails: String? = null

    /**
     * 电子邮件的关键
     */
    var  emailKey: String? = null

    /**
     * 电子邮件网址
     */
    var  emailUrl: String? = null

    /**
     * 端口
     */
    var  port: String? = null
    var  emailName: String? = null
    var  enable: Boolean? = null
}