package cn.hellohao.service;

import cn.hellohao.model.entity.UploadConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface UploadConfigService extends IService<UploadConfig> {
    UploadConfig getUpdateConfig();
    Integer setUpdateConfig(UploadConfig updateConfig);
}
