package cn.hellohao.mapper

import cn.hellohao.model.entity.Config
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ConfigMapper : BaseMapper<Config?> {
    val sourceype: Config?
    fun setSourceype(config: Config?): Int?
}