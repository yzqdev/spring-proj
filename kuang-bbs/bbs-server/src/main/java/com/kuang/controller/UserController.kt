package com.kuang.controller

import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.kuang.model.entity.*
import com.kuang.model.vo.LayerPhoto
import com.kuang.model.vo.LayerPhotoData
import com.kuang.service.BlogService
import com.kuang.service.CommentService
import com.kuang.service.QuestionService
import com.kuang.service.UserInfoService
import com.kuang.utils.KuangUtils.print
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
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
class UserController {
    @Resource
    var userInfoService: UserInfoService? = null

    @Resource
    var blogService: BlogService? = null

    @Resource
    var questionService: QuestionService? = null

    @Resource
    var commentService: CommentService? = null
    @GetMapping("/user/{uid}")
    fun userIndex(@PathVariable uid: String, model: Model): String {
        // 用户信息回填
        userInfoCallBack(uid, model)
        // 用户的博客列表
        val pageParam = Page<Blog>(1, 10)
        blogService!!.page(
            pageParam, QueryWrapper<Blog>().eq("author_id", uid)
                .orderByDesc("gmt_create")
        )
        // 结果
        val blogList = pageParam.records
        model.addAttribute("blogList", blogList)
        model.addAttribute("pageParam", pageParam)
        return "user/index"
    }

    @GetMapping("/user/blog/{uid}/{page}/{limit}")
    fun userIndexBlog(
        @PathVariable uid: String,
        @PathVariable page: Int,
        @PathVariable limit: Int,
        model: Model
    ): String {
        // 用户信息回填
        var page = page
        userInfoCallBack(uid, model)
        // 用户的博客列表
        if (page < 1) {
            page = 1
        }
        val pageParam = Page<Blog>(page.toLong(), limit.toLong())
        blogService!!.page(
            pageParam, QueryWrapper<Blog>().eq("author_id", uid)
                .orderByDesc("gmt_create")
        )

        // 结果
        val blogList = pageParam.records
        model.addAttribute("blogList", blogList)
        model.addAttribute("pageParam", pageParam)
        return "user/index"
    }

    @GetMapping("/user/question/{uid}/{page}/{limit}")
    fun userIndexQuestion(
        @PathVariable uid: String,
        @PathVariable page: Int,
        @PathVariable limit: Int,
        model: Model
    ): String {
        // 用户信息回填
        var page = page
        userInfoCallBack(uid, model)

        //
        if (page < 1) {
            page = 1
        }
        val pageParam = Page<Question>(page.toLong(), limit.toLong())
        questionService!!.page(
            pageParam, QueryWrapper<Question>().eq("author_id", uid)
                .orderByDesc("gmt_create")
        )

        // 结果
        val blogList = pageParam.records
        model.addAttribute("questionList", blogList)
        model.addAttribute("pageParam", pageParam)
        return "user/user-question"
    }

    @GetMapping("/user/comment/{uid}/{page}/{limit}")
    fun userIndexComment(
        @PathVariable uid: String,
        @PathVariable page: Int,
        @PathVariable limit: Int,
        model: Model
    ): String {
        // 用户信息回填
        var page = page
        userInfoCallBack(uid, model)
        //
        if (page < 1) {
            page = 1
        }
        val pageParam = Page<Comment>(page.toLong(), limit.toLong())
        commentService!!.page(
            pageParam, QueryWrapper<Comment>().eq("user_id", uid)
                .orderByDesc("gmt_create")
        )

        // 结果
        val commentList = pageParam.records
        model.addAttribute("commentList", commentList)
        model.addAttribute("pageParam", pageParam)
        return "user/user-comment"
    }

    // 用户信息回填
    private fun userInfoCallBack(uid: String, model: Model) {
        val userInfo = userInfoService!!.getById(uid)
        model.addAttribute("userInfo", userInfo)
        if (userInfo.getHobby() != null && !userInfo.getHobby().equals("")) {
            val hobbys: Array<String> = userInfo.getHobby().split(",")
            model.addAttribute("infoHobbys", hobbys)
        }
        // 获取用户的问题，博客，回复数
        val blogCount = blogService!!.count(QueryWrapper<Blog>().eq("author_id", uid))
        val questionCount = questionService!!.count(QueryWrapper<Question>().eq("author_id", uid))
        val commentCount = commentService!!.count(QueryWrapper<Comment>().eq("user_id", uid))
        model.addAttribute("blogCount", blogCount)
        model.addAttribute("questionCount", questionCount)
        model.addAttribute("commentCount", commentCount)
    }

    // 捐赠layer弹窗二维码
    @GetMapping("/user/donate/{uid}")
    @ResponseBody
    fun userLayerDonate(@PathVariable uid: String?): String {
        // todo: 数据库设计
        val layerPhotos = ArrayList<LayerPhotoData>()
        layerPhotos.add(LayerPhotoData().setAlt("支付宝").setPid(1).setSrc("/images/donate/alipay.png").setThumb(""))
        layerPhotos.add(LayerPhotoData().setAlt("微信").setPid(2).setSrc("/images/donate/wechat.jpg").setThumb(""))
        val donate: LayerPhoto = LayerPhoto().setTitle("赞赏").setId(666).setStart(1)
        donate.setData(layerPhotos)
        val donateJsonString = JSONObject.toJSONString(donate)
        print(donateJsonString)
        return donateJsonString
    }

    // 更新头像
    @GetMapping("/user/update-avatar/{uid}")
    fun toUpdateAvatar(@PathVariable uid: String, model: Model): String {
        // 用户信息回填
        userInfoCallBack(uid, model)
        return "user/update-avatar"
    }
}