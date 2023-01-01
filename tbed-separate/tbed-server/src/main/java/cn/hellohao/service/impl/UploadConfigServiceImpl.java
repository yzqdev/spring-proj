package cn.hellohao.service.impl;

import cn.hellohao.mapper.UploadConfigMapper;
import cn.hellohao.model.entity.UploadConfig;
import cn.hellohao.service.UploadConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadConfigServiceImpl extends ServiceImpl<UploadConfigMapper,UploadConfig> implements UploadConfigService {
    @Autowired
    private UploadConfigMapper uploadConfigMapper;

    @Override
    public UploadConfig getUpdateConfig() {
        return uploadConfigMapper.getUpdateConfig();
    }

    @Override
    public Integer setUpdateConfig(UploadConfig uploadConfig) {
        return uploadConfigMapper.setUpdateConfig(uploadConfig);
    }
}

