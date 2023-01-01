package cn.hellohao.service.impl

import cn.hellohao.mapper.ConfigMapper
import cn.hellohao.model.entity.Config
import cn.hellohao.service.ConfigService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigServiceImpl( private val configMapper: ConfigMapper) : ServiceImpl<ConfigMapper?, Config?>(), ConfigService {

    override fun getSourceType(): Config? {
        return configMapper!!.sourceype
    }

    override fun setSourceType(config: Config?): Int? {
        return configMapper!!.setSourceype(config)
    }
}