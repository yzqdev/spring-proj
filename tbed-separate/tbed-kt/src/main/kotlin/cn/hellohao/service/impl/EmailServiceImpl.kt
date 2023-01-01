package cn.hellohao.service.impl

import cn.hellohao.mapper.EmailConfigMapper
import cn.hellohao.model.entity.EmailConfig
import cn.hellohao.service.EmailConfigService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl( var emailConfigMapper: EmailConfigMapper) : ServiceImpl<EmailConfigMapper, EmailConfig>(),
    EmailConfigService {

    override fun getEmail(): EmailConfig {
        return emailConfigMapper.email
    }

    override fun updateEmail(emailConfig: EmailConfig): Int {
        return emailConfigMapper.updateEmail(emailConfig)
    }
}