package cn.hellohao.exception

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/6 8:51
 */
//创建自定义异常
//
class StorageSourceInitException : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    ) {
    }
}