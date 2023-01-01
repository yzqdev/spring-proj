package cn.hellohao.mapper

import cn.hellohao.model.entity.StorageKey
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface KeysMapper : BaseMapper<StorageKey?> {
    /**
     * 查询密钥
     *
     * @param id id
     * @return [StorageKey]
     */
    fun selectKeys(@Param("id") id: String?): StorageKey?
    val storageName: List<StorageKey?>?
    val storage: List<StorageKey?>?

    //修改key
    val keys: List<StorageKey?>?
}