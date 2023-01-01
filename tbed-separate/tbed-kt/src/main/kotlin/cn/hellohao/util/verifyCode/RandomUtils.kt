package cn.hellohao.util.verifyCode


import java.awt.Color
import java.util.*


object RandomUtils : org.apache.commons.lang3.RandomUtils() {
    private val CODE_SEQ = charArrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
        'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
        'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'
    )
    private val NUMBER_ARRAY = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    private val random = Random()
    fun randomString(length: Int): String {
        val sb = StringBuilder()
        for (i in 0 until length) {
            sb.append(CODE_SEQ[random.nextInt(CODE_SEQ.size)].toString())
        }
        return sb.toString()
    }

    fun randomNumberString(length: Int): String {
        val sb = StringBuilder()
        for (i in 0 until length) {
            sb.append(NUMBER_ARRAY[random.nextInt(NUMBER_ARRAY.size)].toString())
        }
        return sb.toString()
    }

    fun randomColor(fc: Int, bc: Int): Color {
        var f = fc
        var b = bc
        val random = Random()
        if (f > 255) {
            f = 255
        }
        if (b > 255) {
            b = 255
        }
        return Color(f + random.nextInt(b - f), f + random.nextInt(b - f), f + random.nextInt(b - f))
    }

    fun nextInt(bound: Int): Int {
        return random.nextInt(bound)
    }
}