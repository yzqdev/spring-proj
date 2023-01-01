package cn.hellohao.model.dto;

import lombok.Data;

/**
 * @author yanni
 * @date time 2021/11/19 17:24
 * @modified By:
 */
@Data
public class UserLoginDto {
    private String username;
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证代码
     */
    private String verifyCode;
}
