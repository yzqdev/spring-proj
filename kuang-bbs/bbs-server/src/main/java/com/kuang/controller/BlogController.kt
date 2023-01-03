package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.kuang.model.entity.*
import com.kuang.model.vo.QuestionWriteForm
import com.kuang.service.BlogCategoryService
import com.kuang.service.BlogService
import com.kuang.service.CommentService
import com.kuang.utils.KuangUtils.print
import com.kuang.utils.KuangUtils.time
import com.kuang.utils.KuangUtils.uuid
import com.kuang.utils.RequestHelper.sessionUser
import org.springframework.web.bind.annotation.*
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
class BlogController {
    @Resource
    var blogCategoryService: BlogCategoryService? = null

    @Resource
    var blogService: BlogService? = null

    @Resource
    var commentService: CommentService? = null
    @GetMapping("/blogList/{page}/{limit}")
    fun blogListPage(
        @PathVariable page: Int,
        @PathVariable limit: Int
    ): HashMap<*, *> {
        var page = page
        val model = HashMap<String, Any>()
        if (page < 1) {
            page = 1
        }
        val pageParam = Page<Blog>(page.toLong(), limit.toLong())
        blogService!!.page(pageParam, QueryWrapper<Blog>().orderByDesc("gmt_create"))

        // 结果
        val blogList = pageParam.records
        model["blogList"] = blogList
        model["pageParam"] = pageParam

        // 分类信息
        val categoryList = blogCategoryService!!.list(null)
        model["categoryList"] = categoryList
        return model
    }

    // 写文章
    @GetMapping("/blog/write")
    fun toWrite(): HashMap<*, *> {
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        val categoryList = blogCategoryService!!.list(null)
        model["categoryList"] = categoryList
        return model
    }

    @PostMapping("/blog/write")
    @Synchronized
    fun write(questionWriteForm: QuestionWriteForm): HashMap<*, *>? {
        // 构建问题对象
        val blog = Blog()
        blog.setBid(uuid)
        blog.setTitle(questionWriteForm.getTitle())
        blog.setContent(questionWriteForm.getContent())
        blog.setSort(0)
        blog.setViews(0)
        blog.setAuthorId(questionWriteForm.getAuthorId())
        blog.setAuthorName(questionWriteForm.getAuthorName())
        blog.setAuthorAvatar(questionWriteForm.getAuthorAvatar())
        val category = blogCategoryService!!.getById(questionWriteForm.getCategoryId())
        blog.setCategoryId(questionWriteForm.getCategoryId())
        blog.setCategoryName(category.getCategory())
        blog.setGmtCreate(time)
        blog.setGmtUpdate(time)
        // 存储对象
        blogService!!.save(blog)

        // 重定向到列表页面
        return null
    }

    /**
     * 阅读文章
     *
     * @param bid 报价
     * @return [HashMap]
     */
    @GetMapping("/blog/read/{bid}")
    fun read(@PathVariable("bid") bid: String?): HashMap<*, *> {
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        val blog = blogService!!.getOne(QueryWrapper<Blog>().eq("bid", bid))
        // todo: redis缓存. 防止阅读重复
        blog.setViews(blog.getViews() + 1)
        blogService!!.updateById(blog)
        model["blog"] = blog
        // todo： 查询评论
        val commentList = commentService!!.list(QueryWrapper<Comment>().eq("topic_id", bid).orderByDesc("gmt_create"))
        model["commentList"] = commentList
        return model
    }

    // 编辑问题
    @GetMapping("/blog/editor/{uid}/{bid}")
    @Synchronized
    fun toEditor(
        @PathVariable("uid") uid: String?,
        @PathVariable("bid") bid: String?
    ): HashMap<*, *>? {
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        val blog = blogService!!.getOne(QueryWrapper<Blog>().eq("bid", bid))
        if (!blog.getAuthorId().equals(uid)) {
            print("禁止非法编辑")
            return null
        }
        model["blog"] = blog
        val categoryList = blogCategoryService!!.list(null)
        model["categoryList"] = categoryList
        return model
    }

    @GetMapping("/blog/{blogId}")
    fun getBlogById(@PathVariable("blogId") blogId: String?): HashMap<String, Any?>? {
        val blog = blogService!!.getOne(QueryWrapper<Blog>().eq("bid", blogId))
        val userId: String = sessionUser.getUserId()
        if (!blog.getAuthorId().equals(userId)) {
            print("禁止非法编辑")
            return null
        }
        val categoryList = blogCategoryService!!.list(null)
        val res = HashMap<String, Any?>(2)
        res["blog"] = blog
        res["categoryList"] = categoryList
        return res
    }

    @PostMapping("/blog/editor")
    fun editor(blog: Blog): String {
        val queryBlog = blogService!!.getOne(QueryWrapper<Blog>().eq("bid", blog.getBid()))
        queryBlog.setTitle(blog.getTitle())
        queryBlog.setCategoryId(blog.getCategoryId())
        queryBlog.setContent(blog.getContent())
        queryBlog.setGmtUpdate(time)
        blogService!!.updateById(queryBlog)
        return "redirect:/blog/read/" + blog.getBid()
    }

    // 删除问题
    @GetMapping("/blog/delete/{uid}/{bid}")
    fun delete(
        @PathVariable("uid") uid: String?,
        @PathVariable("bid") bid: String?
    ): String {
        val blog = blogService!!.getOne(QueryWrapper<Blog>().eq("bid", bid))
        if (!blog.getAuthorId().equals(uid)) {
            print("禁止非法删除")
            return "redirect:/blog"
        }
        blogService!!.removeById(blog)
        // 重定向到列表页面
        return "redirect:/blog"
    }

    // 评论
    @PostMapping("/blog/comment/{bid}")
    fun comment(@PathVariable("bid") bid: String, comment: Comment): String {
        // 存储评论
        comment.setCommentId(uuid)
        comment.setTopicCategory(1)
        comment.setGmtCreate(time)
        commentService!!.save(comment)
        // 重定向到列表页面
        return "redirect:/blog/read/$bid"
    }
}