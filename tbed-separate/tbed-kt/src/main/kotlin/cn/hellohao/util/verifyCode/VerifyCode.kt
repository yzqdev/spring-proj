package cn.hellohao.util.verifyCode

/**
 * 验证码类
 */
class VerifyCode {
    var code: String? = null
    var imgBytes: ByteArray?=null
    var expireTime: Long = 0
}