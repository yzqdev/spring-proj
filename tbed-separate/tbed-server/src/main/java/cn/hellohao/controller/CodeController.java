package cn.hellohao.controller;

import cn.hellohao.model.entity.Code;
import cn.hellohao.model.entity.Msg;
import cn.hellohao.model.dto.PageDto;
import cn.hellohao.service.CodeService;
import cn.hutool.crypto.SecureUtil;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:25
 */
@RestController
@RequestMapping("/admin/root")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping(value = "/selectCodeList")//new
    public Map<String, Object> selectCodeList(@RequestBody PageDto pageDto) {
        Map<String, Object> map = new HashMap<>();

        Integer pageNum = pageDto.getPageNum();
        Integer pageSize = pageDto.getPageSize();
        Page<Code> page=new Page<>(pageNum,pageSize);
        Page<Code> codes = null;
        try {
            codes=codeService.page(page);


            map.put("code", 200);
            map.put("info", "");
            map.put("count", codes.getTotal());
            map.put("data", codes.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("info", "获取数据异常");
        }
        return map;
    }

    @PostMapping("/deleteCodes")//new

    public Msg deletecodes(@RequestBody List<Code> arr) {
        final Msg msg = new Msg();


        Integer v = 0;
        try {
            for (int i = 0; i < arr.size(); i++) {
                codeService.deleteCode(arr.get(i).getExpandCode());
                v++;
            }
            msg.setInfo("已成功删除" + v + "个扩容码");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("删除过程中发生现错误，已成功删除" + v + "个");
        }
        return msg;
    }


    @PostMapping("/addCode")//new

    public Msg addcode(@RequestBody Map<String, Object> jsonObj) {
        final Msg msg = new Msg();

        Integer value = (Integer) jsonObj.get("memory");
        Integer count = Integer.parseInt((String) jsonObj.get("count"));
        if (value <= 0 || value > 1048576 || count <= 0 || count > 1000) {
            msg.setInfo("数据格式错误,请正确输入");
            return msg;
        }
        Integer val = 0;
        Code code = new Code();
        for (int i = 0; i < count; i++) {
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("hhmmss");
            Integer number = (int) (Math.random() * 100000) + 1;
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0, 5);
            code.setId(uuid);
            code.setValue(Long.toString(value * 1024 * 1024));
            code.setExpandCode(SecureUtil.sha256(number + format1.format(LocalDateTime.now()) + uuid));

            codeService.addCode(code);
            val++;
        }
        msg.setInfo("已成功生成" + val + "个扩容码");
        return msg;
    }


}
