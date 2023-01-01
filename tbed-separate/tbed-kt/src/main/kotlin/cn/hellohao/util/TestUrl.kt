package cn.hellohao.util

import java.net.URL

object TestUrl {
    //最好使用下面这个，上面那个超时时间不定，所以可能会导致卡住的情况
    //    testUrlWithTimeOut("http://tc.hellohao.cn2/getNoticeText", 2000);
    fun testUrl(urlString: String?) {
        val lo = System.currentTimeMillis()
        var url: URL?
        try {
            url = URL(urlString)
            val `in` = url.openStream()
            println("连接可用")
        } catch (e1: Exception) {
            println("连接打不开!")
            url = null
        }
        println(System.currentTimeMillis() - lo)
    }

    fun testUrlWithTimeOut(urlString: String?, timeOutMillSeconds: Int): Boolean {
        val lo = System.currentTimeMillis()
        var url: URL?
        return try {
            url = URL(urlString)
            val co = url.openConnection()
            co.connectTimeout = timeOutMillSeconds
            co.connect()
            true
            //System.out.println("连接可用");
        } catch (e1: Exception) {
            //System.out.println("连接打不开!");
            url = null
            false
        }
    }
}