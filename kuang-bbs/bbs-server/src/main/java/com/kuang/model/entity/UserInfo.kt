package com.kuang.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors
import java.io.Serializable

/**
 *
 *
 *
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_user_info")
@Schema(title = "UserInfo对象", description = "")
class UserInfo : Serializable {
    @Schema(title = "用户id")
    @TableId(value = "uid", type = IdType.INPUT)
    private val uid: String? = null

    @Schema(title = "用户昵称")
    private val nickname: String? = null

    @Schema(title = "真实姓名")
    private val realname: String? = null

    @Schema(title = "QQ")
    private val qq: String? = null

    @Schema(title = "WeChat")
    private val wechat: String? = null

    @Schema(title = "邮箱")
    private val email: String? = null

    @Schema(title = "手机")
    private val phone: String? = null

    @Schema(title = "工作")
    private val work: String? = null

    @Schema(title = "地址")
    private val address: String? = null

    @Schema(title = "爱好")
    private val hobby: String? = null

    @Schema(title = "自我介绍")
    private val intro: String? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}