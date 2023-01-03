package org.sang.service

import org.sang.bean.Category
import org.sang.mapper.CategoryMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

/**
 * Created by sang on 2017/12/19.
 */
@Service
@Transactional
class CategoryService {
    @Autowired
    var categoryMapper: CategoryMapper? = null
    val allCategories: List<Category?>?
        get() = categoryMapper.getAllCategories()

    fun deleteCategoryByIds(ids: String): Boolean {
        val split: Array<String?> = ids.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val result = categoryMapper!!.deleteCategoryByIds(split)
        return result == split.size
    }

    fun updateCategoryById(category: Category?): Int {
        return categoryMapper!!.updateCategoryById(category)
    }

    fun addCategory(category: Category): Int {
        category.date = Timestamp(System.currentTimeMillis())
        return categoryMapper!!.addCategory(category)
    }
}