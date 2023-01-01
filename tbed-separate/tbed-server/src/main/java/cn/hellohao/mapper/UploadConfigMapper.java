package cn.hellohao.mapper;

import cn.hellohao.model.entity.UploadConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 上传配置
 *
 * @author yanni
 * @date 2022/04/02
 */
@Mapper
public interface UploadConfigMapper extends BaseMapper<UploadConfig> {
    /**
     * 得到更新配置
     *
     * @return {@link UploadConfig}
     */
    UploadConfig getUpdateConfig();

    /**
     * 设置更新配置
     *
     * @param uploadConfig 上传配置
     * @return {@link Integer}
     */
    Integer setUpdateConfig(UploadConfig uploadConfig);
}
