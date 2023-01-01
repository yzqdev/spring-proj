package cn.hellohao.service;

import cn.hellohao.model.entity.EmailConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface EmailConfigService extends IService<EmailConfig> {
    EmailConfig getEmail();
    Integer updateEmail(EmailConfig emailConfig);
}
