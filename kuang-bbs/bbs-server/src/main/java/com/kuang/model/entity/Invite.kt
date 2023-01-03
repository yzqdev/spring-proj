package com.kuang.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors
import java.io.Serializable
import java.util.*

/**
 * @author 遇见狂神说
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_invite")
@Schema(title = "Invite邀请码", description = "邀请码")
class Invite : Serializable {
    @Schema(title = "自增id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "邀请码")
    private val code: String? = null

    @Schema(title = "用户id")
    private val uid: String? = null

    @Schema(title = "状态 0 未使用 1 使用")
    private val status: Int? = null

    @Schema(title = "激活时间")
    private val activeTime: Date? = null

    @Schema(title = "创建时间")
    private val gmtCreate: Date? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}