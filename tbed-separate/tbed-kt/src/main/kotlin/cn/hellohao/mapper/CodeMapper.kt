package cn.hellohao.mapper

import cn.hellohao.model.entity.Code
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface CodeMapper : BaseMapper<Code?> {
    /**
     * 查询扩容码
     *
     * @param code 代码
     * @return [Page]<[Code]>
     */
    fun selectCode(@Param("code") code: String?): Page<Code?>?

    /**
     * 选择codekey
     *
     * @param code 代码
     * @return [Code]
     */
    fun selectCodekey(@Param("code") code: String?): Code?
    //Integer selectCodekey(@Param("value") String value)
    /**
     * 添加
     * @param code
     * @return
     */
    fun addCode(code: Code?): Int?

    //删除扩容码
    fun deleteCode(@Param("code") code: String?): Int?
}