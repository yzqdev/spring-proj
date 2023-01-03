package org.sang.controller.admin

import org.sang.bean.RespBean
import org.sang.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin")
class AdminController {
    @Autowired
    var articleService: ArticleService? = null
    @RequestMapping(value = ["/article/all"], method = [RequestMethod.GET])
    fun getArticleByStateByAdmin(
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @RequestParam(value = "count", defaultValue = "6") count: Int,
        keywords: String?
    ): Map<String, Any?> {
        val articles = articleService!!.getArticleByState(-2, page, count, keywords)
        val map: MutableMap<String, Any?> = HashMap()
        map["articles"] = articles
        map["totalCount"] = articleService!!.getArticleCountByState(1, null, keywords)
        return map
    }

    @RequestMapping(value = ["/article/dustbin"], method = [RequestMethod.PUT])
    fun updateArticleState(aids: Array<Long?>, state: Int): RespBean {
        return if (articleService!!.updateArticleState(aids, state) == aids.size) {
            RespBean("success", "删除成功!")
        } else RespBean("error", "删除失败!")
    }
}