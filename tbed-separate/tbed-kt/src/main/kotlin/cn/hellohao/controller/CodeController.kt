package cn.hellohao.controller

import cn.hellohao.model.dto.PageDto
import cn.hellohao.model.entity.Code
import cn.hellohao.model.entity.Msg
import cn.hellohao.service.CodeService
import cn.hutool.crypto.SecureUtil
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:25
 */
@RestController
@RequestMapping("/admin/root")
class CodeController(private val codeService: CodeService) {


    @PostMapping(value = ["/selectCodeList"]) //new
    fun selectCodeList(@RequestBody pageDto: PageDto): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val pageNum: Int = pageDto.pageNum
        val pageSize: Int = pageDto.pageSize
        val page = Page<Code>(pageNum.toLong(), pageSize.toLong())
        val codes: Page<Code>
        try {
            codes = codeService.page(page)
            map["code"] = 200
            map["info"] = ""
            map["count"] = codes.total
            map["data"] = codes.records
        } catch (e: Exception) {
            e.printStackTrace()
            map["code"] = 500
            map["info"] = "获取数据异常"
        }
        return map
    }

    @PostMapping("/deleteCodes") //new
    fun deletecodes(@RequestBody arr: List<Code>): Msg {
        val msg = Msg()
        var v = 0
        try {
            for (i in arr.indices) {
                codeService.deleteCode(arr[i].expandCode)
                v++
            }
            msg.info = "已成功删除" + v + "个扩容码"
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "删除过程中发生现错误，已成功删除" + v + "个"
        }
        return msg
    }

    @PostMapping("/addCode") //new
    fun addcode(@RequestBody jsonObj: Map<String?, Any?>): Msg {
        val msg = Msg()
        val value = jsonObj["memory"] as Int?
        val count: Int = jsonObj["count"] as Int
        if (value!! <= 0 || value > 1048576 || count <= 0 || count > 1000) {
            msg.info = "数据格式错误,请正确输入"
            return msg
        }
        var `val` = 0
        val code = Code()
        for (i in 0 until count) {
            val format1 = DateTimeFormatter.ofPattern("hhmmss")
            val number = (Math.random() * 100000).toInt() + 1
            val uuid = UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault()).substring(0, 5)
            code.id=uuid
            code.value= (value * 1024 * 1024).toString()
            code.expandCode=SecureUtil.sha256(number.toString() + format1.format(LocalDateTime.now()) + uuid)
            codeService.addCode(code)
            `val`++
        }
        msg.info = "已成功生成" + `val` + "个扩容码"
        return msg
    }
}