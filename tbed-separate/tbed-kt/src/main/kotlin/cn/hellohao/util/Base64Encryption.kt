package cn.hellohao.util

import java.util.*

/**
 * Created by Hellohao on 2019-08-27.
 */
object Base64Encryption {
    fun toBaseCode(str: String) {
        val data = Base64Encryption.encryptBASE64(str.toByteArray())
    }

    fun decryptBASE64(key: String?): String {
        var b: ByteArray? = null
        b = Base64.getDecoder().decode(key)
        return String(b)
    }

    fun encryptBASE64(key: ByteArray?): String {
        val string = "SGVsbG9oYW8K"
        return Base64.getEncoder().encodeToString(key).replace("\r|\n".toRegex(), "")
    }
}