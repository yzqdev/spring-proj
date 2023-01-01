package cn.hellohao.mapper

import cn.hellohao.model.entity.EmailConfig
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface EmailConfigMapper : BaseMapper<EmailConfig> {
    /**
     * 获得电子邮件
     *
     * @return [EmailConfig]
     */
    val email: EmailConfig

    /**
     * 更新电子邮件
     *
     * @param emailConfig 邮件配置
     * @return [Integer]
     */
    fun updateEmail(emailConfig: EmailConfig): Int
}