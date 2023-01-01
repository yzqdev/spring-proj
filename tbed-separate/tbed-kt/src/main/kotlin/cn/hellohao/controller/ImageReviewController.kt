package cn.hellohao.controller

import cn.hellohao.model.entity.Imgreview
import cn.hellohao.model.entity.Msg
import cn.hellohao.serviceimport.ImgreviewService
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/admin/root")
class ImageReviewController(  private val imgreviewService: ImgreviewService) {


    @PostMapping("/updateimgReviewConfig") //new
    @ResponseBody
    fun updateimgReviewConfig(@RequestParam(value = "data", defaultValue = "") data: String?): Msg {
        val msg = Msg()
        try {
            val jsonObj = JSONObject.parseObject(data)
            val imgreview: Imgreview = JSON.toJavaObject<Imgreview>(jsonObj, Imgreview::class.java)
            if (imgreview.getId() === "1") {
                if (null == imgreview.getId() || null == imgreview.getApiKey() || null == imgreview.getUsing() || null == imgreview.getSecretKey() || null == imgreview.getAppId() || imgreview.getApiKey()
                        .equals("")
                    || imgreview.getSecretKey().equals("") || imgreview.getAppId().equals("")
                ) {
                    msg.code = "110400"
                    msg.info = "各参数不能为空"
                    return msg
                }
            } else {
                if (null == imgreview.getId() || null == imgreview.getApiKey() || null == imgreview.getUsing() || imgreview.getApiKey()
                        .equals("")
                ) {
                    msg.code = "110400"
                    msg.info = "各参数不能为空"
                    return msg
                }
            }
            if (null == imgreviewService.selectByPrimaryKey(imgreview.getId())) {
                imgreviewService.insert(imgreview)
            } else {
                imgreviewService.updateByPrimaryKeySelective(imgreview)
            }
            msg.info = "保存成功"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "保存过程出现错误"
        }
        return msg
    }
}