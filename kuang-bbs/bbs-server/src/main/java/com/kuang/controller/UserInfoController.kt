package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.kuang.model.entity.*
import com.kuang.service.BlogService
import com.kuang.service.CommentService
import com.kuang.service.QuestionService
import com.kuang.service.UserInfoService
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
 * @since 2020-06-29
 */
@RestController
@RequestMapping
class UserInfoController {
    @Resource
    var userInfoService: UserInfoService? = null

    @Resource
    var blogService: BlogService? = null

    @Resource
    var questionService: QuestionService? = null

    @Resource
    var commentService: CommentService? = null

    // 更新用户资料
    @GetMapping("/userinfo/setting/{uid}")
    fun userSetting(@PathVariable uid: String, model: Model): String {
        // 用户信息回填
        userInfoCallBack(uid, model)
        // todo: 可扩展
        return "user/settings"
    }

    @PostMapping("/userinfo/update/{uid}")
    fun userInfo(@PathVariable uid: String, userInfo: UserInfo): String {
        // 获取用户信息;
        userInfoService!!.updateById(userInfo)
        return "redirect:/user/$uid"
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
}