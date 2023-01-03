package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.kuang.model.entity.Blog
import com.kuang.service.BlogCategoryService
import com.kuang.service.BlogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

/**
 *
 *
 * 前端控制器
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-29
 */
@RestController
@RequestMapping
class BlogCategoryController {
    @Resource
    var blogCategoryService: BlogCategoryService? = null

    @Resource
    var blogService: BlogService? = null
    @GetMapping("/blog/category/{bid}/{page}/{limit}")
    fun blogPage(
        @PathVariable bid: Int,
        @PathVariable page: Int,
        @PathVariable limit: Int
    ): HashMap<String, Any> {
        var page = page
        if (page < 1) {
            page = 1
        }
        val model = HashMap<String, Any>()
        // 查询这个分类下的所有问题，获取查询的数据信息
        val pageParam = Page<Blog>(page.toLong(), limit.toLong())
        blogService!!.page(
            pageParam, QueryWrapper<Blog>()
                .eq("category_id", bid).orderByDesc("gmt_create")
        )
        val records = pageParam.records
        model["blogList"] = records
        model["pageParam"] = pageParam

        // 查询这个分类信息
        val category = blogCategoryService!!.getById(bid)
        model["thisCategoryName"] = category.getCategory()

        // 全部分类信息
        val categoryList = blogCategoryService!!.list(null)
        model["categoryList"] = categoryList
        return model
    }
}