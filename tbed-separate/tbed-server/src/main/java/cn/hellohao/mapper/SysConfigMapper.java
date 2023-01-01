package cn.hellohao.mapper;

import cn.hellohao.model.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:33
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    /**
     * getstate
     *
     * @return {@link SysConfig}
     */
    SysConfig getDefaultSysConfig();

    /**
     * 设置状态
     *
     * @param sysConfig 系统配置
     * @return {@link Integer}
     */
    Integer setDefaultSysConfig(SysConfig sysConfig);
}
