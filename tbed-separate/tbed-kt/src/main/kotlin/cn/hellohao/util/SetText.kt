package cn.hellohao.util

import java.util.*
import java.util.regex.Pattern

object SetText {
    @kotlin.jvm.JvmStatic
    fun getSubString(text: String, left: String?, right: String?): String {
        var result = ""
        var zLen: Int
        if (left == null || left.isEmpty()) {
            zLen = 0
        } else {
            zLen = text.indexOf(left)
            if (zLen > -1) {
                zLen += left.length
            } else {
                zLen = 0
            }
        }
        var yLen = text.indexOf(right!!, zLen)
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length
        }
        result = text.substring(zLen, yLen)
        return result
    }

    //获取8位短uuid
    var chars = arrayOf(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
        "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
        "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"
    )
    @kotlin.jvm.JvmStatic
    val shortUuid: String
        get() {
            val shortBuffer = StringBuffer()
            val uuid = UUID.randomUUID().toString().replace("-", "")
            for (i in 0..7) {
                val str = uuid.substring(i * 4, i * 4 + 4)
                val x = str.toInt(16)
                shortBuffer.append(chars[x % 0x3E])
            }
            return shortBuffer.toString()
        }

    fun get32UUID(): String {
        return UUID.randomUUID().toString().trim { it <= ' ' }.replace("-".toRegex(), "")
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun checkEmail(email: String?): Boolean {
        var flag = false
        flag = try {
            val check = "^\\w+@[a-zA-Z0-9]{2,10}(?:\\.[a-z]{2,4}){1,3}$"
            val regex = Pattern.compile(check)
            val matcher = regex.matcher(email)
            matcher.matches()
        } catch (e: Exception) {
            false
        }
        return flag
    }
}