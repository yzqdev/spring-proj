package com.kuang.result

import io.swagger.v3.oas.annotations.media.Schema
import lombok.AllArgsConstructor
import lombok.Data

@Data

@Schema(title = "全局统一返回结果")
class BaseResponse<T>(success: Boolean, code: Int, message: String, data: T) {
    @Schema(title = "是否成功")
     val success: Boolean? = null

    @Schema(title = "返回码")
     val code: Int? = null

    @Schema(title = "返回消息")
    val message: String? = null

    @Schema(title = "返回数据")
 val data: T? = null

    companion object {
        fun <T> ok(data: T): BaseResponse<T> {
            return BaseResponse(
                ResultCodeEnum.SUCCESS.success,
                ResultCodeEnum.SUCCESS.code,
                ResultCodeEnum.SUCCESS.message,
                data
            )
        }

        fun <T> error(): BaseResponse<T> {
            return BaseResponse(
                ResultCodeEnum.UNKNOWN_REASON.success,
                ResultCodeEnum.UNKNOWN_REASON.code,
                ResultCodeEnum.UNKNOWN_REASON.message,
                null
            )
        }

        fun <T> setResult(resultCodeEnum: ResultCodeEnum): BaseResponse<T> {
            return BaseResponse(resultCodeEnum.success, resultCodeEnum.code, resultCodeEnum.message, null)
        }
    }
}