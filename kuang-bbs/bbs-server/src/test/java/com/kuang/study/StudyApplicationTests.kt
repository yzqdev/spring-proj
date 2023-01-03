package com.kuang.study

import cn.hutool.log.LogFactory
import cn.hutool.log.StaticLog
import cn.hutool.log.dialect.console.ConsoleColorLogFactory
import cn.hutool.log.dialect.console.ConsoleLogFactory
import org.junit.jupiter.api.Test

internal class StudyApplicationTests {
    @Test
    fun colorLog() {

        //ConsoleColorLog.setColorFactory(AnsiColor.YELLOW);
        LogFactory.setCurrentLogFactory(ConsoleColorLogFactory::class.java)
        StaticLog.debug("This is static {} log", "debug")
        StaticLog.info("This is static {} log", "info")
        StaticLog.error("This is static {} log", "error")
        StaticLog.warn("This is static {} log", "warn")
        StaticLog.trace("This is static {} log", "trace")
    }

    @Test
    fun commonLog() {
        LogFactory.setCurrentLogFactory(ConsoleLogFactory::class.java)
        StaticLog.debug("This is static {} log", "debug")
        StaticLog.info("This is static {} log", "info")
    }
}