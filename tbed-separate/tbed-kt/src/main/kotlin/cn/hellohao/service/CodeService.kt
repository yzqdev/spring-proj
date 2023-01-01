package cn.hellohao.service

import cn.hellohao.model.entity.Code
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:20
 */
@Service
interface CodeService : IService<Code?> {
    //查询扩容码
    fun selectCode(value: String?): Page<Code?>?
    fun selectCodeKey(code: String?): Code?

    /**
     * 添加代码
     *
     * @param code 代码
     * @return [Integer]
     */
    fun addCode(code: Code?): Int?

    //删除扩容码
    fun deleteCode(code: String?): Int?
}