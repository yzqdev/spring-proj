package cn.hellohao.mapper;

import cn.hellohao.model.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
    Config getSourceype();
    Integer setSourceype(Config config);
}
