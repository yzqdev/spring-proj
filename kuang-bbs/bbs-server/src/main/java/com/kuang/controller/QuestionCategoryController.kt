package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.kuang.model.entity.*
import com.kuang.service.QuestionCategoryService
import com.kuang.service.QuestionService
import org.springframework.ui.Model
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
 * @since 2020-06-28
 */
@RestController
@RequestMapping
class QuestionCategoryController {
    @Resource
    var questionCategoryService: QuestionCategoryService? = null

    @Resource
    var questionService: QuestionService? = null
    @GetMapping("/question/category/{cid}/{page}/{limit}")
    fun questionPage(
        @PathVariable cid: Int,
        @PathVariable page: Int,
        @PathVariable limit: Int,
        model: Model
    ): String {
        var page = page
        if (page < 1) {
            page = 1
        }

        // 查询这个分类下的所有问题，获取查询的数据信息
        val pageParam = Page<Question>(page.toLong(), limit.toLong())
        questionService!!.page(
            pageParam, QueryWrapper<Question>()
                .eq("category_id", cid).orderByDesc("gmt_create")
        )
        val records = pageParam.records
        model.addAttribute("questionList", records)
        model.addAttribute("pageParam", pageParam)

        // 查询这个分类信息
        val category = questionCategoryService!!.getById(cid)
        model.addAttribute("thisCategoryName", category.getCategory())

        // 全部分类信息
        val categoryList = questionCategoryService!!.list(null)
        model.addAttribute("categoryList", categoryList)
        return "question/list"
    }
}