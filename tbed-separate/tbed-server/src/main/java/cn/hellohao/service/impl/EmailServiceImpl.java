package cn.hellohao.service.impl;

import cn.hellohao.mapper.EmailConfigMapper;
import cn.hellohao.model.entity.EmailConfig;
import cn.hellohao.service.EmailConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl extends ServiceImpl<EmailConfigMapper,EmailConfig> implements EmailConfigService {
    @Autowired
    EmailConfigMapper emailConfigMapper;
    @Override
    public EmailConfig getEmail() {
        return emailConfigMapper.getEmail();
    }

    @Override
    public Integer updateEmail(EmailConfig emailConfig) {
        return emailConfigMapper.updateEmail(emailConfig);
    }
}
