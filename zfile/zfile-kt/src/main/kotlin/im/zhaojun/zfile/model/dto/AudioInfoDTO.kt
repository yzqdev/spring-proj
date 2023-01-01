package im.zhaojun.zfile.model.dto

import lombok.Data

/**
 * @author zhaojun
 */
@Data
class AudioInfoDTO {
    var  title: String? = null
    var  artist: String? = null
    var  cover: String? = null
    var  src: String? = null

    companion object {
        fun buildDefaultAudioInfoDTO(): AudioInfoDTO {
            val audioInfoDTO = AudioInfoDTO().apply {
                title="未知歌曲"
                artist="未知"
                cover="http://c.jun6.net/audio.png"
            }

            return audioInfoDTO
        }
    }
}