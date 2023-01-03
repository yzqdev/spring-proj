package org.sang.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.sang.bean.Article

/**
 * Created by sang on 2017/12/20.
 */
@Mapper
interface ArticleMapper {
    fun addNewArticle(article: Article?): Int
    fun updateArticle(article: Article?): Int
    fun getArticleByState(
        @Param("state") state: Int?,
        @Param("start") start: Int?,
        @Param("count") count: Int?,
        @Param("uid") uid: Long?,
        @Param("keywords") keywords: String?
    ): List<Article?>?

    //    List<Article> getArticleByStateByAdmin(@Param("start") int start, @Param("count") Integer count, @Param("keywords") String keywords);
    fun getArticleCountByState(
        @Param("state") state: Int?,
        @Param("uid") uid: Long?,
        @Param("keywords") keywords: String?
    ): Int

    fun updateArticleState(@Param("aids") aids: Array<Long?>?, @Param("state") state: Int?): Int
    fun updateArticleStateById(@Param("articleId") articleId: Int?, @Param("state") state: Int?): Int
    fun deleteArticleById(@Param("aids") aids: Array<Long?>?): Int
    fun getArticleById(aid: Long?): Article?
    fun pvIncrement(aid: Long?)

    //INSERT INTO pv(countDate,pv,uid) SELECT NOW(),SUM(pageView),uid FROM article GROUP BY uid
    fun pvStatisticsPerDay()
    fun getCategories(uid: Long?): List<String?>?
    fun getDataStatistics(uid: Long?): List<Int?>?
}