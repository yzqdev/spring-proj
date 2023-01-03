package org.sang.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.sang.bean.Category

/**
 * Created by sang on 2017/12/19.
 */
@Mapper
interface CategoryMapper {
    val allCategories: List<Category?>?
    fun deleteCategoryByIds(@Param("ids") ids: Array<String?>?): Int
    fun updateCategoryById(category: Category?): Int
    fun addCategory(category: Category?): Int
}