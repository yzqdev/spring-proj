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
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_say")
@Schema(title = "Say对象", description = "")
class Say : Serializable {
    @Schema(title = "唯一id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "标题")
    private val title: String? = null

    @Schema(title = "内容")
    private val content: String? = null

    @Schema(title = "时间")
    private val gmtCreate: Date? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}