package cn.hellohao.service

import cn.hellohao.model.entity.EmailConfig
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

@Service
interface EmailConfigService : IService<EmailConfig> {
    fun getEmail(): EmailConfig
    fun updateEmail(emailConfig: EmailConfig): Int
}