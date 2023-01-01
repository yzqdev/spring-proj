package me.zhengjie.utils

import me.zhengjie.utils.DateUtil.Companion.fromTimeStamp
import me.zhengjie.utils.DateUtil.Companion.localDateTimeFormatyMdHms
import me.zhengjie.utils.DateUtil.Companion.toLocalDateTime
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DateUtilsTest {
    @Test
    fun test1() {
        val l = System.currentTimeMillis() / 1000
        val localDateTime = fromTimeStamp(l)
        print(localDateTimeFormatyMdHms(localDateTime))
    }

    @Test
    fun test2() {
        val now = LocalDateTime.now()
        println(localDateTimeFormatyMdHms(now))
        val date = DateUtil.toDate(now)
        val localDateTime = toLocalDateTime(date)
        println(localDateTimeFormatyMdHms(localDateTime))
        val localDateTime1 = fromTimeStamp(date.time / 1000)
        println(localDateTimeFormatyMdHms(localDateTime1))
    }
}