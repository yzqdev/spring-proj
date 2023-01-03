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
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_comment")
@Schema(title = "Comment对象", description = "")
class Comment : Serializable {
    @Schema(title = "自增id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "评论唯一id")
    private val commentId: String? = null

    @Schema(title = "1博客 2问答")
    private val topicCategory: Int? = null

    @Schema(title = "评论主题id")
    private val topicId: String? = null

    @Schema(title = "评论者id")
    private val userId: String? = null

    @Schema(title = "评论者昵称")
    private val userName: String? = null

    @Schema(title = "评论者头像")
    private val userAvatar: String? = null

    @Schema(title = "评论内容")
    private val content: String? = null

    @Schema(title = "评论创建时间")
    private val gmtCreate: Date? = null

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}