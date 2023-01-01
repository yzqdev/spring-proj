package cn.hellohao.service.impl;

import cn.hellohao.mapper.SysConfigMapper;
import cn.hellohao.model.entity.SysConfig;
import cn.hellohao.service.SysConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:48
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper,SysConfig> implements SysConfigService {
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Override
    public SysConfig getstate() {
        return sysConfigMapper.getDefaultSysConfig();
    }

    @Override
    public Integer setstate(SysConfig sysConfig) {
        return sysConfigMapper.setDefaultSysConfig(sysConfig);
    }
}
