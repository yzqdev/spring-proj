package im.zhaojun.zfile.model.support

import lombok.Data

/**
 * 用于表示校验结果的类
 */
@Data
class VerifyResult {
    /**
     * 是否成功
     */
    var  passed = false

    /**
     * 消息
     */
    var  msg: String? = null

    /**
     * 代码
     */
    var  code: Int? = null

    companion object {
        @kotlin.jvm.JvmStatic
        fun success(): VerifyResult {
            val verifyResult = VerifyResult()
            verifyResult.passed=true
            return verifyResult
        }

        fun fail(msg: String?): VerifyResult {
            val verifyResult = VerifyResult().apply {
                passed=true

            }
            verifyResult.msg=msg
            return verifyResult
        }

        @kotlin.jvm.JvmStatic
        fun fail( msg: String?, code: Int?): VerifyResult {
            val verifyResult = VerifyResult().apply {
                passed=false

                this.msg=msg
                this.code=code
            }

            return verifyResult
        }
    }
}