package im.zhaojun.zfile.model.constant

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * @author zhaojun
 */
@Configuration
class ZFileConstant {
    @Autowired(required = false)
    fun setTmpFilePath(@Value("\${zfile.tmp.path}") tmpFilePath: String) {
        TMP_FILE_PATH = tmpFilePath
    }

    @Autowired(required = false)
    fun setHeaderFileName(@Value("\${zfile.constant.readme}") headerFileName: String) {
        README_FILE_NAME = headerFileName
    }

    @Autowired(required = false)
    fun setPasswordFileName(@Value("\${zfile.constant.password}") passwordFileName: String) {
        PASSWORD_FILE_NAME = passwordFileName
    }

    @Autowired(required = false)
    fun setAudioMaxFileSizeMb(@Value("\${zfile.preview.audio.maxFileSizeMb}") maxFileSizeMb: Long) {
        AUDIO_MAX_FILE_SIZE_MB = maxFileSizeMb
    }

    @Autowired(required = false)
    fun setTextMaxFileSizeMb(@Value("\${zfile.preview.text.maxFileSizeKb}") maxFileSizeKb: Long) {
        TEXT_MAX_FILE_SIZE_KB = maxFileSizeKb
    }

    @Autowired(required = false)
    fun setDirectLinkPrefix(@Value("\${zfile.directLinkPrefix}") directLinkPrefix: String) {
        DIRECT_LINK_PREFIX = directLinkPrefix
    }

    companion object {
        val USER_HOME = System.getProperty("user.home")
        const val PATH_SEPARATOR_CHAR = '/'
        const val PATH_SEPARATOR = "/"

        /**
         * 直链前缀名称
         */
        @kotlin.jvm.JvmField
        var DIRECT_LINK_PREFIX = "directlink"

        /**
         * 系统产生的临时文件路径
         */
        var TMP_FILE_PATH = "/.zfile/tmp2/"

        /**
         * 页面文档文件
         */
        @kotlin.jvm.JvmField
        var README_FILE_NAME = "readme.md"

        /**
         * 密码文件
         */
        @kotlin.jvm.JvmField
        var PASSWORD_FILE_NAME = "password.txt"

        /**
         * 最大支持文件大小为 ? MB 的音乐文件解析封面, 歌手等信息.
         */
        var AUDIO_MAX_FILE_SIZE_MB = 1L

        /**
         * 最大支持文本文件大小为 ? KB 的文件内容.
         */
        var TEXT_MAX_FILE_SIZE_KB = 100L
    }
}