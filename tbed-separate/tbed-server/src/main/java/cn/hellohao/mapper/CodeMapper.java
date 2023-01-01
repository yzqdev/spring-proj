package cn.hellohao.mapper;

import cn.hellohao.model.entity.Code;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CodeMapper extends BaseMapper<Code> {
    /**
     * 查询扩容码
     *
     * @param code 代码
     * @return {@link Page}<{@link Code}>
     */
    Page<Code> selectCode(@Param("code") String code);

    /**
     * 选择codekey
     *
     * @param code 代码
     * @return {@link Code}
     */
    Code selectCodekey(@Param("code") String code);
    //Integer selectCodekey(@Param("value") String value)

    /**
     * 添加
     * @param code
     * @return
     */
    Integer addCode(Code code);
    //删除扩容码
    Integer deleteCode(@Param("code") String code);

}
