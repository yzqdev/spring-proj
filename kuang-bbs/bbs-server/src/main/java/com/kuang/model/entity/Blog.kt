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
import java.util.*

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
@TableName("ks_blog")
@Schema(title = "Blog对象", description = "")
class Blog : Serializable {
    @Schema(title = "自增id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "博客id")
    private val bid: String? = null

    @Schema(title = "博客标题")
    private val title: String? = null

    @Schema(title = "博客内容")
    private val content: String? = null

    @Schema(title = "排序 0 普通  1 置顶")
    private val sort: Int? = null

    @Schema(title = "浏览量")
    private val views: Int? = null

    @Schema(title = "作者id")
    private val authorId: String? = null

    @Schema(title = "作者名")
    private val authorName: String? = null

    @Schema(title = "作者头像")
    private val authorAvatar: String? = null

    @Schema(title = "问题分类id")
    private val categoryId: Int? = null

    @Schema(title = "问题分类名称")
    private val categoryName: String? = null

    @Schema(title = "创建时间")
    private val gmtCreate: Date? = null

    @Schema(title = "修改时间")
    private val gmtUpdate: Date? = null

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}