package com.kuang.model.vo

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
class QuestionWriteForm {
    @Schema(title = "问题标题")
    private val title: String? = null

    @Schema(title = "问题内容")
    private val content: String? = null

    @Schema(title = "问题分类id")
    private val categoryId: Int? = null

    @Schema(title = "作者id")
    private val authorId: String? = null

    @Schema(title = "作者名称")
    private val authorName: String? = null

    @Schema(title = "作者头像")
    private val authorAvatar: String? = null
}