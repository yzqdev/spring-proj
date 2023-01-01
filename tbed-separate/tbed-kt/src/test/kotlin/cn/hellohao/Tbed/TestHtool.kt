package cn.hellohao.Tbed

import cn.hutool.system.SystemUtil
import org.junit.Test

/**
 * @author yanni
 * @date time 2022/5/4 0:02
 * @modified By:
 */
class TestHtool {
    @get:Test
    val os: Unit
        get() {
            println(SystemUtil.getOsInfo())
            val props = System.getProperties()
            println(props.getProperty("user.home"))
        }
}