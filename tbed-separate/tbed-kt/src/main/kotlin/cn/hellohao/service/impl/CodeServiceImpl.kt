package cn.hellohao.service.impl

import cn.hellohao.mapper.CodeMapper
import cn.hellohao.model.entity.Code
import cn.hellohao.service.CodeService
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:21
 */
@Service
class CodeServiceImpl(  private val codeMapper: CodeMapper) : ServiceImpl<CodeMapper?, Code?>(), CodeService {

    override fun selectCode(value: String?): Page<Code?>? {
        return codeMapper!!.selectCode(value)
    }

    override fun selectCodeKey(code: String?): Code? {
        return codeMapper!!.selectCodekey(code)
    }

    override fun addCode(code: Code?): Int? {
        return codeMapper!!.addCode(code)
    }

    override fun deleteCode(code: String?): Int? {
        return codeMapper!!.deleteCode(code)
    }
}