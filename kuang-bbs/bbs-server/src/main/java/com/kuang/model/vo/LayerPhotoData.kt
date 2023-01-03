package com.kuang.model.vo

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
class LayerPhotoData {
    @Schema(title = "图片名")
    private val alt: String? = null

    @Schema(title = "图片id")
    private val pid = 0

    @Schema(title = "原图地址")
    private val src: String? = null

    @Schema(title = "缩略图地址")
    private val thumb: String? = null
}