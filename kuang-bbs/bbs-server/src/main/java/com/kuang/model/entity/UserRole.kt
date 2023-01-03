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
 *
 *
 *
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_user_role")
@Schema(title = "UserRole对象", description = "")
class UserRole : Serializable {
    @Schema(title = "角色编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "角色名称")
    private val name: String? = null

    @Schema(title = "角色描述")
    private val description: String? = null

    @Schema(title = "创建时间")
    private val gmtCreate: Date? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}