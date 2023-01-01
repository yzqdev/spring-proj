package cn.hellohao.service.impl

import cn.hellohao.service.MobilePaperService
import cn.hellohao.util.ToStringTools
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-09-01 21:34
 */
@Service
class MobilePaperServiceImpl : MobilePaperService {
    private val ISURL = "687474703a2f2f736572766963652e7069636173736f2e616465736b2e636f6d2f76312f766572746963616c"
    override fun GetMobilepaper(start: Int, count: Int, category: String?): String {
        val buffer = StringBuffer()
        var httpConn: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var strURL: String? = null
        try {
            strURL = if (category == null) {
                ToStringTools.decodeString(ISURL) + "/vertical?limit=" + count + "&skip=" + start + "&adult=false&first=" + start + "&order=hot"
            } else {
                ToStringTools.decodeString(ISURL) + "/category/" + category + "/vertical?limit=" + count + "&skip=" + start + "&adult=false&first=" + start + "&order=hot"
            }
            val url = URL(strURL)
            httpConn = url.openConnection() as HttpURLConnection
            httpConn!!.requestMethod = "GET"
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

    override fun GetMobilepaperCategory(): String {
        val CategoryJson = StringBuffer()
        var httpConn: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            val strURL: String = ToStringTools.decodeString(ISURL) + "/category"
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