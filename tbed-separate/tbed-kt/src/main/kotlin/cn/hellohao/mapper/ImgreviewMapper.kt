package cn.hellohao.mapper

import cn.hellohao.model.entity.Imgreview
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ImgreviewMapper : BaseMapper<Imgreview?> {
    fun deleteByPrimaryKey(id: Int?): Int
    override fun insert(record: Imgreview?): Int
    fun insertSelective(record: Imgreview?): Int
    fun selectByPrimaryKey(@Param("id") id: String?): Imgreview?
    fun selectByusing(@Param("using") using: Int?): Imgreview?
    fun updateByPrimaryKeySelective(record: Imgreview?): Int
    fun updateByPrimaryKey(record: Imgreview?): Int
}