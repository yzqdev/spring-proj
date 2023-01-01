package im.zhaojun.zfile.exception

/**
 * 存储策略未初始化异常
 * @author zhaojun
 */
class StorageStrategyUninitializedException : RuntimeException {
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

    companion object {
        private const val serialVersionUID = 5736940575583615661L
    }
}