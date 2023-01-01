package cn.hellohao.util

import java.io.UnsupportedEncodingException

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020-07-25 23:03
 */
object ToStringTools {
    fun decodeString(str: String?): String {
        if (str == null) {
            return "err"
        }
        val s = ToStringTools.pack(str)
        val gbk: String
        gbk = try {
            String(s, "gbk")
        } catch (ignored: UnsupportedEncodingException) {
            "err"
        }
        return gbk
    }

    fun pack(str: String): ByteArray {
        var nibbleshift = 4
        var position = 0
        val len = str.length / 2 + str.length % 2
        val output = ByteArray(len)
        for (v in str.toCharArray()) {
            var n = v.code.toByte()
            if (n >= '0'.code.toByte() && n <= '9'.code.toByte()) {
                (n -= '0'.code.toByte()).toByte()
            } else if (n >= 'A'.code.toByte() && n <= 'F'.code.toByte()) {
                (n -= ('A'.code - 10).toByte()).toByte()
            } else if (n >= 'a'.code.toByte() && n <= 'f'.code.toByte()) {
                (n -= ('a'.code - 10).toByte()).toByte()
            } else {
                continue
            }
            output[position] = (output[position].toInt() or (n.toInt() shl nibbleshift)).toByte()
            if (nibbleshift == 0) {
                position++
            }
            nibbleshift = nibbleshift + 4 and 7
        }
        return output
    }
}