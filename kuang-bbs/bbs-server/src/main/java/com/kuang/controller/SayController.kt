package com.kuang.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.kuang.model.entity.Say
import com.kuang.service.SayService
import com.kuang.utils.KuangUtils.time
import com.kuang.utils.KuangUtils.uuid
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

@RestController
@RequestMapping
class SayController {
    @Resource
    var sayService: SayService? = null
    @GetMapping("/say")
    fun userIndexBlog(model: Model): String {
        val pageParam = Page<Say>(1, 50)
        sayService!!.page(pageParam, QueryWrapper<Say>().orderByDesc("gmt_create"))
        // 结果
        val sayList = pageParam.records
        model.addAttribute("sayList", sayList)
        model.addAttribute("pageParam", pageParam)
        return "page/say"
    }

    @PostMapping("/say/{role}")
    fun saveSay(@PathVariable("role") role: Int, say: Say): String {
        // 防止请求提交
        if (role != 1) {
            return "redirect:/say"
        }
        say.setId(uuid)
        say.setGmtCreate(time)
        // 结果
        sayService!!.save(say)
        return "redirect:/say"
    }
}