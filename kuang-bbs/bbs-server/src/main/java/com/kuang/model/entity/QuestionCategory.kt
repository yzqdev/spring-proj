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
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ks_question_category")
@Schema(title = "QuestionCategory对象", description = "")
class QuestionCategory : Serializable {
    @Schema(title = "自增id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private val id: String? = null

    @Schema(title = "问题分类")
    private val category: String? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}