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
@TableName("ks_blog_category")
@Schema(title = "BlogCategory对象", description = "")
class BlogCategory : Serializable {
    @Schema(title = "自增id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "博客分类")
    private val category: String? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}