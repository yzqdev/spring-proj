package cn.hellohao.util

import cn.hutool.core.lang.Console
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/9/20 16:27
 */
object DateUtils {
    //计算时间
    fun plusDay(setday: Int): LocalDateTime {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val today = LocalDateTime.now()
        Console.log("现在的日期是：$today")
        val enddate = today.plusDays(setday.toLong())
        Console.log("到期的日期：" + enddate.format(format))
        return enddate
    }
}