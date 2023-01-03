package com.kuang.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors
import java.io.Serial
import java.io.Serializable

/**
 *
 *
 *
 *
 *
 * @author 遇见狂神说
 * @since 2020-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_download")
@Schema(title = "Download对象", description = "")
class Download : Serializable {
    @Schema(title = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "资源名")
    private val dname: String? = null

    @Schema(title = "资源链接")
    private val ddesc: String? = null

    @Schema(title = "提取码")
    private val dcode: String? = null

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}