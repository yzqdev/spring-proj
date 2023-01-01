package im.zhaojun.zfile.exception

/**
 * 对象存储初始化异常
 * @author zhaojun
 */
class InitializeDriveException : RuntimeException {
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
        private const val serialVersionUID = -1920550904063819880L
    }
}