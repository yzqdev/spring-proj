package com.kuang.controller

import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.kuang.model.entity.*
import com.kuang.model.vo.QuestionWriteForm
import com.kuang.service.CommentService
import com.kuang.service.QuestionCategoryService
import com.kuang.service.QuestionService
import com.kuang.utils.KuangUtils.print
import com.kuang.utils.KuangUtils.time
import com.kuang.utils.KuangUtils.uuid
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

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
class QuestionController {
    @Resource
    var questionCategoryService: QuestionCategoryService? = null

    @Resource
    var questionService: QuestionService? = null

    @Resource
    var commentService: CommentService? = null

    // 问题列表展示
    @GetMapping("/questionList/{page}/{limit}")
    fun questionListPage(
        @PathVariable page: Int,
        @PathVariable limit: Int
    ): HashMap<*, *> {
        var page = page
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        if (page < 1) {
            page = 1
        }
        val pageParam = Page<Question>(page.toLong(), limit.toLong())
        questionService!!.page(pageParam, QueryWrapper<Question>().orderByDesc("gmt_create"))

        // 结果
        val questionList = pageParam.records
        model["questionList"] = questionList
        model["pageParam"] = pageParam

        // 分类信息
        val categoryList = questionCategoryService!!.list(null)
        model["categoryList"] = categoryList
        return model
    }

    /**
     * 发布问题
     *
     * @return [Object]
     */
    @GetMapping("/question/category")
    fun toWrite(): Any {
        return questionCategoryService!!.list(null)
    }

    @PostMapping("/question/write")
    @Synchronized
    fun write(questionWriteForm: QuestionWriteForm): String {
        // 构建问题对象
        val question = Question()
        question.setQid(uuid)
        question.setTitle(questionWriteForm.getTitle())
        question.setContent(questionWriteForm.getContent())
        question.setStatus(0)
        question.setSort(0)
        question.setViews(0)
        question.setAuthorId(questionWriteForm.getAuthorId())
        question.setAuthorName(questionWriteForm.getAuthorName())
        question.setAuthorAvatar(questionWriteForm.getAuthorAvatar())
        val category = questionCategoryService!!.getById(questionWriteForm.getCategoryId())
        question.setCategoryId(questionWriteForm.getCategoryId())
        question.setCategoryName(category.getCategory())
        question.setGmtCreate(time)
        question.setGmtUpdate(time)
        // 存储对象
        questionService!!.save(question)

        // 重定向到列表页面
        return "redirect:/question"
    }

    // 阅读问题
    @GetMapping("/question/read/{qid}")
    fun read(@PathVariable("qid") qid: String?): HashMap<*, *> {
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        val question = questionService!!.getOne(QueryWrapper<Question>().eq("qid", qid))
        // todo: redis缓存. 防止阅读重复
        question.setViews(question.getViews() + 1)
        questionService!!.updateById(question)
        model["question"] = question
        // todo： 查询评论.
        val commentList = commentService!!.list(QueryWrapper<Comment>().eq("topic_id", qid).orderByDesc("gmt_create"))
        model["commentList"] = commentList
        return model
    }

    // 评论
    @PostMapping("/question/comment/{qid}")
    fun comment(@PathVariable("qid") qid: String, comment: Comment): String {
        // 存储评论
        comment.setCommentId(uuid)
        comment.setTopicCategory(2)
        comment.setGmtCreate(time)
        commentService!!.save(comment)
        // 状态改为已解决
        val question = questionService!!.getOne(QueryWrapper<Question>().eq("qid", qid))
        question.setStatus(1)
        questionService!!.updateById(question)
        // 重定向到列表页面
        return "redirect:/question/read/$qid"
    }

    // 编辑问题
    @GetMapping("/question/editor/{uid}/{qid}")
    @Synchronized
    fun toEditor(
        @PathVariable("uid") uid: String?,
        @PathVariable("qid") qid: String?
    ): String {
        val model: HashMap<*, *> = HashMap<Any?, Any?>()
        val question = questionService!!.getOne(QueryWrapper<Question>().eq("qid", qid))
        if (!question.getAuthorId().equals(uid)) {
            print("禁止非法编辑")
            return "redirect:/question"
        }
        model["question"] = question
        val categoryList = questionCategoryService!!.list(null)
        model["categoryList"] = categoryList
        return "question/editor"
    }

    @PostMapping("/question/editor")
    fun editor(question: Question): String {
        val queryQuestion = questionService!!.getOne(QueryWrapper<Question>().eq("qid", question.getQid()))
        queryQuestion.setTitle(question.getTitle())
        queryQuestion.setCategoryId(question.getCategoryId())
        queryQuestion.setContent(question.getContent())
        queryQuestion.setGmtUpdate(time)
        questionService!!.updateById(queryQuestion)
        return "redirect:/question/read/" + question.getQid()
    }

    // 删除问题
    @GetMapping("/question/delete/{uid}/{qid}")
    fun delete(
        @PathVariable("uid") uid: String?,
        @PathVariable("qid") qid: String?
    ): String {
        val question = questionService!!.getOne(QueryWrapper<Question>().eq("qid", qid))
        if (!question.getAuthorId().equals(uid)) {
            print("禁止非法删除")
            return "redirect:/question"
        }
        questionService!!.removeById(question)

        // 重定向到列表页面
        return "redirect:/question"
    }

    // md 文件上传
    @Operation(summary = "md文件上传问题")
    @PostMapping("/question/write/file/upload")
    @Throws(
        IOException::class
    )
    fun fileUpload(
        @RequestParam(value = "editormd-image-file", required = true) file: MultipartFile,
        request: HttpServletRequest?
    ): JSONObject {

        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        var path = System.getProperty("user.dir") + "/upload/"

        //按照月份进行分类：
        val instance = Calendar.getInstance()
        val month = (instance[Calendar.MONTH] + 1).toString() + "月"
        path = path + month
        val realPath = File(path)
        if (!realPath.exists()) {
            realPath.mkdir()
        }

        //上传文件地址
        print("上传文件保存地址：$realPath")

        //解决文件名字问题：我们使用uuid;
        val filename = "ks-" + UUID.randomUUID().toString().replace("-".toRegex(), "")
        val originalFilename = file.originalFilename!!
        val i = originalFilename.lastIndexOf(".")
        val suffix = originalFilename.substring(i + 1)
        val outFilename = "$filename.$suffix"
        print("文件名：$outFilename")

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(File("$realPath/$outFilename"))

        //给editormd进行回调
        val res = JSONObject()
        res["url"] = "/upload/$month/$outFilename"
        res["success"] = 1
        res["message"] = "upload success!"
        print(res.toJSONString())
        return res
    }
}