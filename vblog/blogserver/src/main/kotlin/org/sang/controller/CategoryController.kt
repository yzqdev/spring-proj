package org.sang.controller

import org.sang.bean.Category
import org.sang.bean.RespBean
import org.sang.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin/category")
class CategoryController {
    @Autowired
    var categoryService: CategoryService? = null

    @get:RequestMapping(value = ["/all"], method = [RequestMethod.GET])
    val allCategories: List<Category?>?
        get() = categoryService.getAllCategories()

    @RequestMapping(value = ["/{ids}"], method = [RequestMethod.DELETE])
    fun deleteById(@PathVariable ids: String): RespBean {
        val result = categoryService!!.deleteCategoryByIds(ids)
        return if (result) {
            RespBean("success", "删除成功!")
        } else RespBean("error", "删除失败!")
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun addNewCate(category: Category): RespBean {
        if ("" == category.cateName || category.cateName == null) {
            return RespBean("error", "请输入栏目名称!")
        }
        val result = categoryService!!.addCategory(category)
        return if (result == 1) {
            RespBean("success", "添加成功!")
        } else RespBean("error", "添加失败!")
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.PUT])
    fun updateCate(category: Category?): RespBean {
        val i = categoryService!!.updateCategoryById(category)
        return if (i == 1) {
            RespBean("success", "修改成功!")
        } else RespBean("error", "修改失败!")
    }
}