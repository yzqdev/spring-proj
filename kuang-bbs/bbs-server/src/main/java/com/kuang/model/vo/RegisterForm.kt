package com.kuang.model.vo

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors

/**
 * 注册表单
 *
 * @author yanni
 * @date 2021/11/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
class RegisterForm {
    @Schema(title = "用户名")
    private val username: String? = null

    @Schema(title = "密码")
    private val password: String? = null

    @Schema(title = "确认密码")
    private val repassword: String? = null

    @Schema(title = "邀请码")
    private val code: String? = null
}