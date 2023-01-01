package cn.hellohao.Tbed

import org.junit.Test

/**
 * @author yanni
 * @date time 2022/5/16 0:36
 * @modified By:
 */
class DriverTest {
    @get:Test
    val pg: Unit
        get() {
            val DBDRIVER = "org.postgresql.Driver"
            try {
                Class.forName(DBDRIVER)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
}