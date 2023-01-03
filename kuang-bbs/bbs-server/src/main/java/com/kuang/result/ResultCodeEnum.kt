package com.kuang.result

import lombok.Getter

/**
 * 枚举结果代码
 *
 * @author yanni
 * @date 2021/11/22
 */
@Getter
enum class ResultCodeEnum(
    /**
     * 成功
     */
    val success: Boolean,
    /**
     * 代码
     */
    val code: Int,
    /**
     * 消息
     */
    val message: String
) {
    /**
     * 成功
     */
    SUCCESS(true, 20000, "成功"),

    /**
     * 未知的原因
     */
    UNKNOWN_REASON(false, 20001, "未知错误");
}