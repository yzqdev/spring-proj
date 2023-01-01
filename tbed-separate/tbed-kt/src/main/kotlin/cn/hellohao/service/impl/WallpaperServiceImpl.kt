package cn.hellohao.service.impl


import cn.hellohao.service.WallpaperService
import cn.hellohao.util.ToStringTools
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Service
class WallpaperServiceImpl : WallpaperService {
    private val ISURL = "687474703a2f2f77616c6c70617065722e6170632e3336302e636e2f696e6465782e706870"
    override fun getWallpaper(start: Int, count: Int, category: Int): String {
        val buffer = StringBuffer()
        var httpConn: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var strURL: String? = null
        try {
            strURL = if (category < 1) {
                StringBuilder().append(ToStringTools.decodeString(ISURL))
                    .append("?c=WallPaper&a=getAppsByOrder&order=create_time&start=").append(start).append("&count=")
                    .append(count).append("&from=360chrome").toString()
            } else {
                ToStringTools.decodeString(ISURL) + "?c=WallPaper&a=getAppsByCategory&cid=" + category + "&start=" + start + "&count=" + count + "&from=360chrome"
            }
            val url = URL(strURL)
            println(strURL)
            httpConn = url.openConnection() as HttpURLConnection
            httpConn!!.requestMethod = "GET"
            //httpConn.setRequestProperty("Authorization", author);
            httpConn.connect()
            reader = BufferedReader(InputStreamReader(httpConn.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                buffer.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpConn?.disconnect()
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return buffer.toString()
    }

    override fun wallpaperCategory(): String
        {
            val CategoryJson = StringBuffer()
            var httpConn: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val strURL: String = ToStringTools.decodeString(ISURL) + "?c=WallPaper&a=getAllCategoriesV2"
                val url = URL(strURL)
                httpConn = url.openConnection() as HttpURLConnection
                httpConn!!.requestMethod = "GET"
                httpConn.connect()
                reader = BufferedReader(InputStreamReader(httpConn.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    CategoryJson.append(line)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpConn?.disconnect()
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return CategoryJson.toString()
        }
}