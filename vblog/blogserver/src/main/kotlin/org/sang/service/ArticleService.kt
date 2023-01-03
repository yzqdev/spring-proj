package org.sang.service

import org.sang.bean.Article
import org.sang.mapper.ArticleMapper
import org.sang.mapper.TagsMapper
import org.sang.utils.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

/**
 * Created by sang on 2017/12/20.
 */
@Service
@Transactional
class ArticleService {
    @Autowired
    var articleMapper: ArticleMapper? = null

    @Autowired
    var tagsMapper: TagsMapper? = null
    fun addNewArticle(article: Article): Int {
        //处理文章摘要
        if (article.summary == null || "" == article.summary) {
            //直接截取
            val stripHtml = stripHtml(article.htmlContent)
            article.summary = stripHtml.substring(0, if (stripHtml.length > 50) 50 else stripHtml.length)
        }
        return if (article.id == -1L) {
            //添加操作
            val timestamp = Timestamp(System.currentTimeMillis())
            if (article.state == 1) {
                //设置发表日期
                article.publishDate = timestamp
            }
            article.editTime = timestamp
            //设置当前用户
            article.uid = Util.getCurrentUser().id
            val i = articleMapper!!.addNewArticle(article)
            //打标签
            val dynamicTags = article.dynamicTags
            if (dynamicTags != null && dynamicTags.size > 0) {
                val tags = addTagsToArticle(dynamicTags, article.id)
                if (tags == -1) {
                    return tags
                }
            }
            i
        } else {
            val timestamp = Timestamp(System.currentTimeMillis())
            if (article.state == 1) {
                //设置发表日期
                article.publishDate = timestamp
            }
            //更新
            article.editTime = Timestamp(System.currentTimeMillis())
            val i = articleMapper!!.updateArticle(article)
            //修改标签
            val dynamicTags = article.dynamicTags
            if (dynamicTags != null && dynamicTags.size > 0) {
                val tags = addTagsToArticle(dynamicTags, article.id)
                if (tags == -1) {
                    return tags
                }
            }
            i
        }
    }

    private fun addTagsToArticle(dynamicTags: Array<String?>, aid: Long?): Int {
        //1.删除该文章目前所有的标签
        tagsMapper!!.deleteTagsByAid(aid)
        //2.将上传上来的标签全部存入数据库
        tagsMapper!!.saveTags(dynamicTags)
        //3.查询这些标签的id
        val tIds = tagsMapper!!.getTagsIdByTagName(dynamicTags)
        //4.重新给文章设置标签
        val i = tagsMapper!!.saveTags2ArticleTags(tIds, aid)
        return if (i == dynamicTags.size) i else -1
    }

    fun stripHtml(content: String?): String {
        var content = content
        content = content!!.replace("<p .*?>".toRegex(), "")
        content = content.replace("<br\\s*/?>".toRegex(), "")
        content = content.replace("\\<.*?>".toRegex(), "")
        return content
    }

    fun getArticleByState(state: Int?, page: Int, count: Int, keywords: String?): List<Article?>? {
        val start = (page - 1) * count
        val uid = Util.getCurrentUser().id
        return articleMapper!!.getArticleByState(state, start, count, uid, keywords)
    }

    //    public List<Article> getArticleByStateByAdmin(Integer page, Integer count,String keywords) {
    //        int start = (page - 1) * count;
    //        return articleMapper.getArticleByStateByAdmin(start, count,keywords);
    //    }
    fun getArticleCountByState(state: Int?, uid: Long?, keywords: String?): Int {
        return articleMapper!!.getArticleCountByState(state, uid, keywords)
    }

    fun updateArticleState(aids: Array<Long?>?, state: Int): Int {
        return if (state == 2) {
            articleMapper!!.deleteArticleById(aids)
        } else {
            articleMapper!!.updateArticleState(aids, 2) //放入到回收站中
        }
    }

    fun restoreArticle(articleId: Int?): Int {
        return articleMapper!!.updateArticleStateById(articleId, 1) // 从回收站还原在原处
    }

    fun getArticleById(aid: Long?): Article? {
        val article = articleMapper!!.getArticleById(aid)
        articleMapper!!.pvIncrement(aid)
        return article
    }

    fun pvStatisticsPerDay() {
        articleMapper!!.pvStatisticsPerDay()
    }

    /**
     * 获取最近七天的日期
     * @return
     */
    val categories: List<String?>?
        get() = articleMapper!!.getCategories(Util.getCurrentUser().id)

    /**
     * 获取最近七天的数据
     * @return
     */
    val dataStatistics: List<Int?>?
        get() = articleMapper!!.getDataStatistics(Util.getCurrentUser().id)
}