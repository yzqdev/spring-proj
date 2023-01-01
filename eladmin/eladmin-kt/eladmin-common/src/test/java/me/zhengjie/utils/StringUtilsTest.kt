package me.zhengjie.utils

import me.zhengjie.utils.StringUtils.getIp
import me.zhengjie.utils.StringUtils.toCapitalizeCamelCase
import me.zhengjie.utils.StringUtils.toUnderScoreCase
import me.zhengjie.utils.StringUtils.weekDay
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import java.text.SimpleDateFormat
import java.util.*

class StringUtilsTest {
    @Test
    fun testToCamelCase() {
    }

    @Test
    fun testToCapitalizeCamelCase() {
        Assertions.assertNull(toCapitalizeCamelCase(null))
        Assertions.assertEquals("HelloWorld", toCapitalizeCamelCase("hello_world"))
    }

    @Test
    fun testToUnderScoreCase() {
        Assertions.assertNull(toUnderScoreCase(null))
        Assertions.assertEquals("hello_world", toUnderScoreCase("helloWorld"))
        Assertions.assertEquals("\u0000\u0000", toUnderScoreCase("\u0000\u0000"))
        Assertions.assertEquals("\u0000_a", toUnderScoreCase("\u0000A"))
    }

    @Test
    fun testGetWeekDay() {
        val simpleDateformat = SimpleDateFormat("E")
        Assertions.assertEquals(simpleDateformat.format(Date()), weekDay)
    }

    @Test
    fun testGetIP() {
        Assertions.assertEquals("127.0.0.1", getIp(MockHttpServletRequest()))
    }
}