import org.junit.Test
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author yanni
 * @date time 2022/3/29 1:05
 * @modified By:
 */
class TestFile {
    @Test
    @Throws(UnsupportedEncodingException::class)
    fun decodeUrl() {
        val url = "D:\\Video\\u000cilms\\%E4%B8%8B%E8%BD%BD%20(19).mp4"
        val dec = URLDecoder.decode(url, "utf-8")
        println(dec)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val path = Paths.get("E:\\downloads\\a.mp4")
            var contentType: String? = null
            try {
                contentType = Files.probeContentType(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            println("File content type is : $contentType")
        }
    }
}