package cn.hellohao.util

import java.io.BufferedReaderimport

java.io.IOExceptionimport java.io.InputStreamReaderimport java.net.URL
object Sentence {
    /**
     * 根据URL请求json数据
     *
     * @return //parm：请求的url链接  返回的是json字符串
     */
    val uRLContent: String
        get() {
            var result = ""
            val urlName = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1"
            try {
                val realURL = URL(urlName)
                val conn = realURL.openConnection()
                conn.setRequestProperty("accept", "*/*")
                conn.setRequestProperty("connection", "Keep-Alive")
                conn.setRequestProperty(
                    "user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36"
                )
                conn.connect()
                val map = conn.headerFields
                val `in` = BufferedReader(InputStreamReader(conn.getInputStream(), "utf-8"))
                var line: String
                while (`in`.readLine().also { line = it } != null) {
                    result += """
                    
                    $line
                    """.trimIndent()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }
}