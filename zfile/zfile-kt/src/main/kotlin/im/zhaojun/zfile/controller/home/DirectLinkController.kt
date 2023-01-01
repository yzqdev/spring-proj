package im.zhaojun.zfile.controller.home

import cn.hutool.core.io.FileUtil
import cn.hutool.core.lang.Console
import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.exception.NotEnabledDriveException
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.service.DriveConfigService
import im.zhaojun.zfile.util.HttpUtil.getTextContent
import org.springframework.stereotype.Controller
import org.springframework.util.AntPathMatcher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.HandlerMapping
import java.io.IOException
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 直链 Controller
 * @author Zhao Jun
 */
@Controller
class DirectLinkController {
    @Resource
    private val driveContext: DriveContext? = null

    @Resource
    private val driveConfigService: DriveConfigService? = null

    /**
     * 获取指定驱动器, 某个文件的直链, 然后重定向过去.
     * @param   driveId
     * 驱动器 ID
     *
     * @return  重定向至文件直链
     */
    @GetMapping("/\${zfile.directLinkPrefix}/{driveId}/**")
    @Throws(IOException::class)
    fun directlink(
        @PathVariable("driveId") driveId: String?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String? {
        val driveConfig = driveConfigService!!.findById(driveId!!)
        val enable: Boolean = driveConfig.enable
        if (java.lang.Boolean.FALSE == enable) {
            throw NotEnabledDriveException()
        }
        val path = request.getAttribute(
            HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE
        ) as String
        val bestMatchPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) as String
        val apm = AntPathMatcher()
        Console.log("bestMatchPattern: {}", bestMatchPattern)
        Console.log("path: {}", path)
        var filePath = apm.extractPathWithinPattern(bestMatchPattern, path)
        Console.log(filePath)
        Console.log("filePath: {}", filePath)
        if (filePath.length > 0 && filePath[0] != ZFileConstant.PATH_SEPARATOR_CHAR) {
            filePath = "/$filePath"
        }
        val fileService = driveContext!![driveId]
        val fileItem: FileItemDTO? = fileService.getFileItem(filePath)
        var url: String = fileItem.src
        if (StrUtil.equalsIgnoreCase(FileUtil.extName(fileItem.name), "m3u8")) {
            val textContent = getTextContent(url)
            response.contentType = "application/vnd.apple.mpegurl;charset=utf-8"
            val out = response.writer
            out.write(textContent)
            out.flush()
            out.close()
            return null
        }
        val queryIndex = url.indexOf('?')
        url = if (queryIndex != -1) {
            val origin = url.substring(0, queryIndex)
            val queryString = url.substring(queryIndex + 1)
            URLUtil.encode(origin) + "?" + URLUtil.encode(queryString)
        } else {
            URLUtil.encode(url)
        }
        return if (fileItem.type == FileTypeEnum.FOLDER) {
            "redirect:" + fileItem.src
        } else {
            "redirect:$url"
        }
    }
}