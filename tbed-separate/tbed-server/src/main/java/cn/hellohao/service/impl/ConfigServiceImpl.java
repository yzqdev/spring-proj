package cn.hellohao.service.impl;

import cn.hellohao.mapper.ConfigMapper;
import cn.hellohao.model.entity.Config;
import cn.hellohao.service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper,Config> implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;
    @Override
    public Config getSourceType() {
        return configMapper.getSourceype();
    }

    @Override
    public Integer setSourceType(Config config) {
        return configMapper.setSourceype(config);
    }
}
