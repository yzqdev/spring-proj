package cn.hellohao.mapper;

import cn.hellohao.model.entity.StorageKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KeysMapper extends BaseMapper<StorageKey> {

    /**
     * 查询密钥
     *
     * @param id id
     * @return {@link StorageKey}
     */
    StorageKey selectKeys(@Param("id") String id);
    List<StorageKey> getStorageName();
    List<StorageKey> getStorage();
    //修改key
    List<StorageKey> getKeys();

}
