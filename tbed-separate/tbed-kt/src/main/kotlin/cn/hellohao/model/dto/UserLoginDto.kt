package cn.hellohao.model.dto

import lombok.Data

/**
 * @author yanni
 * @date time 2021/11/19 17:24
 * @modified By:
 */
@Data
class UserLoginDto {
    var  username: String? = null
    var  email: String? = null

    /**
     * 密码
     */
    var  password: String? = null

    /**
     * 验证代码
     */
    var  verifyCode: String? = null
}