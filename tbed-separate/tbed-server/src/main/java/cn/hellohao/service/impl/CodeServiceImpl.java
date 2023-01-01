package cn.hellohao.service.impl;

import cn.hellohao.mapper.CodeMapper;
import cn.hellohao.model.entity.Code;
import cn.hellohao.service.CodeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:21
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper,Code> implements CodeService {

    @Autowired
    private CodeMapper codeMapper;


    @Override
    public Page<Code> selectCode(String value) {
        return codeMapper.selectCode(value);
    }

    @Override
    public Code selectCodeKey(String code) {
        return codeMapper.selectCodekey(code);
    }

    @Override
    public Integer addCode(Code code) {
        return codeMapper.addCode(code);
    }

    @Override
    public Integer deleteCode(String code) {
        return codeMapper.deleteCode(code);
    }
}
