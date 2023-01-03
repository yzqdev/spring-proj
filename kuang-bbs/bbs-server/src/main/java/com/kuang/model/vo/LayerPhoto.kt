package com.kuang.model.vo

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.experimental.Accessors

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
class LayerPhoto {
    @Schema(title = "相册标题")
    private val title: String? = null

    @Schema(title = "相册id")
    private val id = 0

    @Schema(title = "初始显示的图片序号，默认0")
    private val start = 0

    @Schema(title = "相册包含的图片，数组格式")
    private val data: List<LayerPhotoData>? = null
}