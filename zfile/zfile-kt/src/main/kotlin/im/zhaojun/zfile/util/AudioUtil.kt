package im.zhaojun.zfile.util

import cn.hutool.core.codec.Base64
import cn.hutool.core.io.FileUtil
import cn.hutool.core.lang.UUID
import cn.hutool.core.util.URLUtil
import com.mpatric.mp3agic.*
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.AudioInfoDTO
import lombok.extern.slf4j.Slf4j
import java.io.File
import java.io.IOException
import java.net.URL

/**
 * 音频解析工具类
 * @author zhaojun
 */
@Slf4j
object AudioUtil {
    @JvmStatic
    @Throws(Exception::class)
    fun getAudioInfo(url: String): AudioInfoDTO {
        var url = url
        val query = URL(URLUtil.decode(url)).query
        if (query != null) {
            url = url.replace(query, URLUtil.encode(query))
        }

        // 如果音乐文件大小超出 5M, 则不解析此音乐
        if (HttpUtil.getRemoteFileSize(url) > 1024 * 1024 * ZFileConstant.AUDIO_MAX_FILE_SIZE_MB) {
            return AudioInfoDTO.buildDefaultAudioInfoDTO()
        }
        val fullFilePath =
            StringUtils.removeDuplicateSeparator(ZFileConstant.TMP_FILE_PATH + ZFileConstant.PATH_SEPARATOR + UUID.fastUUID())
        val file = File(fullFilePath)
        FileUtil.mkParentDirs(file)
        cn.hutool.http.HttpUtil.downloadFile(url, file)
        val audioInfoDTO = parseAudioInfo(file)
        audioInfoDTO.src = url
        file.deleteOnExit()
        return audioInfoDTO
    }

    @Throws(IOException::class, UnsupportedTagException::class)
    private fun parseAudioInfo(file: File): AudioInfoDTO {
        val audioInfoDTO = AudioInfoDTO.buildDefaultAudioInfoDTO()
        var mp3File: Mp3File? = null
        try {
            mp3File = Mp3File(file)
        } catch (e: InvalidDataException) {
            if (AudioUtil.log.isDebugEnabled()) {
                AudioUtil.log.debug("无法解析的音频文件.")
            }
        }
        if (mp3File == null) {
            return audioInfoDTO
        }
        var audioTag: ID3v1? = null
        if (mp3File.hasId3v2Tag()) {
            val id3v2Tag = mp3File.id3v2Tag
            val albumImage = id3v2Tag.albumImage
            if (albumImage != null) {
                audioInfoDTO.cover = "data:" + id3v2Tag.albumImageMimeType + ";base64," + Base64.encode(albumImage)
            }
            audioTag = id3v2Tag
        }
        if (audioTag != null) {
            audioInfoDTO.title = audioTag.title
            audioInfoDTO.artist = audioTag.artist
        }
        return audioInfoDTO
    }
}