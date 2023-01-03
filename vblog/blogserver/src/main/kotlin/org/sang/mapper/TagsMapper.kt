package org.sang.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * Created by sang on 2017/12/21.
 */
@Mapper
interface TagsMapper {
    fun deleteTagsByAid(aid: Long?): Int
    fun saveTags(@Param("tags") tags: Array<String?>?): Int
    fun getTagsIdByTagName(@Param("tagNames") tagNames: Array<String?>?): List<Long?>?
    fun saveTags2ArticleTags(@Param("tagIds") tagIds: List<Long?>?, @Param("aid") aid: Long?): Int
}