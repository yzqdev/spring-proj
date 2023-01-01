package cn.hellohao.model.base

/**
 * 枚举了一些常用API操作码
 *
 * @author macro
 * @date 2019/4/19
 */
enum class ResultCode(override val code: Long, override val message: String) : IErrorCode {
    /**
     * resultCode
     */
    SUCCESS(200, "操作成功"), FAILED(500, "操作失败"), VALIDATE_FAILED(404, "参数检验失败"), UNAUTHORIZED(
        401,
        "暂未登录或token已经过期"
    ),
    FORBIDDEN(403, "没有相关权限");

}