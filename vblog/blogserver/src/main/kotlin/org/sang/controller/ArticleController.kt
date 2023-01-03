package org.sang.controller

import org.apache.commons.io.IOUtils
import org.sang.bean.Article
import org.sang.bean.RespBean
import org.sang.service.ArticleService
import org.sang.utils.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sang on 2017/12/20.
 */
@RestController
@RequestMapping("/article")
class ArticleController {
    private val sdf = SimpleDateFormat("yyyyMMdd")

    @Autowired
    var articleService: ArticleService? = null
    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun addNewArticle(article: Article): RespBean {
        val result = articleService!!.addNewArticle(article)
        return if (result == 1) {
            RespBean("success", article.id.toString() + "")
        } else {
            RespBean("error", if (article.state == 0) "文章保存失败!" else "文章发表失败!")
        }
    }

    /**
     * 上传图片
     *
     * @return 返回值为图片的地址
     */
    @RequestMapping(value = ["/uploadimg"], method = [RequestMethod.POST])
    fun uploadImg(req: HttpServletRequest, image: MultipartFile): RespBean {
        val url = StringBuffer()
        val filePath = "/blogimg/" + sdf.format(Date())
        val imgFolderPath = req.servletContext.getRealPath(filePath)
        val imgFolder = File(imgFolderPath)
        if (!imgFolder.exists()) {
            imgFolder.mkdirs()
        }
        url.append(req.scheme)
            .append("://")
            .append(req.serverName)
            .append(":")
            .append(req.serverPort)
            .append(req.contextPath)
            .append(filePath)
        val imgName = UUID.randomUUID().toString() + "_" + image.originalFilename.replace(" ".toRegex(), "")
        try {
            IOUtils.write(image.bytes, FileOutputStream(File(imgFolder, imgName)))
            url.append("/").append(imgName)
            return RespBean("success", url.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return RespBean("error", "上传失败!")
    }

    @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
    fun getArticleByState(
        @RequestParam(value = "state", defaultValue = "-1") state: Int?,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @RequestParam(value = "count", defaultValue = "6") count: Int,
        keywords: String?
    ): Map<String, Any?> {
        val totalCount = articleService!!.getArticleCountByState(state, Util.getCurrentUser().id, keywords)
        val articles = articleService!!.getArticleByState(state, page, count, keywords)
        val map: MutableMap<String, Any?> = HashMap()
        map["totalCount"] = totalCount
        map["articles"] = articles
        return map
    }

    @RequestMapping(value = ["/{aid}"], method = [RequestMethod.GET])
    fun getArticleById(@PathVariable aid: Long?): Article? {
        return articleService!!.getArticleById(aid)
    }

    @RequestMapping(value = ["/dustbin"], method = [RequestMethod.PUT])
    fun updateArticleState(aids: Array<Long?>, state: Int): RespBean {
        return if (articleService!!.updateArticleState(aids, state) == aids.size) {
            RespBean("success", "删除成功!")
        } else RespBean("error", "删除失败!")
    }

    @RequestMapping(value = ["/restore"], method = [RequestMethod.PUT])
    fun restoreArticle(articleId: Int?): RespBean {
        return if (articleService!!.restoreArticle(articleId) == 1) {
            RespBean("success", "还原成功!")
        } else RespBean("error", "还原失败!")
    }

    @RequestMapping("/dataStatistics")
    fun dataStatistics(): Map<String, Any?> {
        val map: MutableMap<String, Any?> = HashMap()
        val categories = articleService.getCategories()
        val dataStatistics = articleService.getDataStatistics()
        map["categories"] = categories
        map["ds"] = dataStatistics
        return map
    }
}