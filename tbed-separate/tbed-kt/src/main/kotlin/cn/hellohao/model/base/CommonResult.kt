package cn.hellohao.model.base

/**
 * 通用返回对象
 * Created by macro on 2019/4/19.
 */
class CommonResult<T> {
    var code: Long = 0
    var message: String? = null
    var data: T? = null
        private set

    protected constructor() {}
    protected constructor(code: Long, message: String?, data: T) {
        this.code = code
        this.message = message
        this.data = data
    }

    fun setData(data: T) {
        this.data = data
    }

    companion object {
        /**
         * 成功返回结果
         *
         * @param data 获取的数据
         */
        fun <T> success(data: T): CommonResult<T> {
            return CommonResult(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message, data)
        }

        /**
         * 成功返回结果
         *
         * @param data 获取的数据
         * @param  message 提示信息
         */
        fun <T> success(data: T, message: String?): CommonResult<T> {
            return CommonResult(ResultCode.SUCCESS.code, message, data)
        }

        /**
         * 失败返回结果
         * @param errorCode 错误码
         */
        fun <T> failed(errorCode: IErrorCode): CommonResult<T?> {
            return CommonResult(errorCode.code, errorCode.message, null)
        }

        /**
         * 失败返回结果
         * @param errorCode 错误码
         * @param message 错误信息
         */
        fun <T> failed(errorCode: IErrorCode, message: String?): CommonResult<T?> {
            return CommonResult(errorCode.code, message, null)
        }

        /**
         * 失败返回结果
         * @param message 提示信息
         */
        fun <T> failed(message: String?): CommonResult<T?> {
            return CommonResult(ResultCode.FAILED.code, message, null)
        }

        /**
         * 失败返回结果
         */
        fun <T> failed(): CommonResult<T> {
            return failed(ResultCode.FAILED)
        }

        /**
         * 参数验证失败返回结果
         */
        fun <T> validateFailed(): CommonResult<T> {
            return failed(ResultCode.VALIDATE_FAILED)
        }

        /**
         * 参数验证失败返回结果
         * @param message 提示信息
         */
        fun <T> validateFailed(message: String?): CommonResult<T?> {
            return CommonResult(ResultCode.VALIDATE_FAILED.code, message, null)
        }

        /**
         * 未登录返回结果
         */
        fun <T> unauthorized(data: T): CommonResult<T> {
            return CommonResult(ResultCode.UNAUTHORIZED.code, ResultCode.UNAUTHORIZED.message, data)
        }

        /**
         * 未授权返回结果
         */
        fun <T> forbidden(data: T): CommonResult<T> {
            return CommonResult(ResultCode.FORBIDDEN.code, ResultCode.FORBIDDEN.message, data)
        }
    }
}