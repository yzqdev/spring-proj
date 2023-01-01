package im.zhaojun.zfile.controller.home

import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.model.support.ResultBean.Companion.successData
import im.zhaojun.zfile.util.AudioUtil
import im.zhaojun.zfile.util.HttpUtil.getTextContent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 文件解析 Controller
 * @author zhaojun
 */
@RestController
@RequestMapping("/common")
class FileParseController {
    /**
     * 获取文件内容, 仅限用于 txt, md, ini 等普通文本文件.
     *
     * @param   url
     * 文件路径
     *
     * @return  文件内容
     */
    @GetMapping("/content")
    fun getContent(url: String?): ResultBean {
        return successData(getTextContent(url!!))
    }

    /**
     * 获取音频文件信息
     *
     * @param   url
     * 文件 URL
     *
     * @return 音频信息, 标题封面等信息
     */
    @GetMapping("/audio-info")
    @Throws(Exception::class)
    fun getAudioInfo(@RequestParam("url") url: String?): ResultBean {
        return ResultBean.success(AudioUtil.getAudioInfo(url!!))
    }
}