package com.kuang.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.io.Serial
import java.io.Serializable
import java.sql.Timestamp

/**
 *
 *
 *
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-28
 */
@Data
@Schema(title = "User对象", description = "")
@TableName("ks_user")
class User : Model<User?>(), Serializable {
    @Schema(title = "自增id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "用户编号")
    private val userId: String? = null

    @Schema(title = "角色编号")
    private val roleId: Int? = null

    @Schema(title = "用户名")
    private val username: String? = null

    @Schema(title = "密码")
    private val password: String? = null

    @Schema(title = "头像")
    private val avatar: String? = null

    @Schema(title = "登录时间")
    private val loginDate: Timestamp? = null

    @Schema(title = "创建时间")
    private val gmtCreate: Timestamp? = null

    companion object {
        @Serial
        private val serialVersionUID = 2603954883132545471L
    }
}