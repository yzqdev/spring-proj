package im.zhaojun.zfile.controller.home

import cn.hutool.core.lang.Console
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.service.impl.LocalServiceImpl
import im.zhaojun.zfile.util.FileUtil.export
import im.zhaojun.zfile.util.StringUtils.removeDuplicateSeparator
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 本地存储 Controller
 * @author zhaojun
 */
@Controller
class LocalController(  private val driveContext: DriveContext) {


    /**
     * 下附件
     * 本地存储下载指定文件
     *
     * @param driveId  驱动器 ID
     * @param type     附件预览类型:
     * download:下载
     * default: 浏览器默认行为
     * @param path     路径 //todo 这里使用的pathmatcher,是2.6的默认值,注意设置
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/file/{driveId}/{*path}")
    @ResponseBody
    @Throws(UnsupportedEncodingException::class)
    fun downAttachment(
        @PathVariable("driveId") driveId: String?,
        @PathVariable("path") path: String,
        type: String?,
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ) {
        val localService = driveContext!![driveId] as LocalServiceImpl
        val realUrl = URLDecoder.decode(
            removeDuplicateSeparator(localService.filePath + ZFileConstant.PATH_SEPARATOR + path),
            "utf-8"
        )
        Console.log(realUrl)
        val file = File(realUrl)
        export(request!!, response!!, file, type)
    }
}