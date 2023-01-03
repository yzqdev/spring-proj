package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.kuang.model.entity.Comment
import com.kuang.service.CommentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

/**
 *
 *
 * 前端控制器
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-30
 */
@RestController
class CommentController {
    @Resource
    var commentService: CommentService? = null

    // 删除评论
    @GetMapping("/user/comment/delete/{uid}/{cid}")
    fun deleteComment(@PathVariable uid: String, @PathVariable cid: String?): String {
        commentService!!.remove(QueryWrapper<Comment>().eq("comment_id", cid))
        return "redirect:/user/comment/$uid/1/10"
    }
}