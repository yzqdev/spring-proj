package cn.hellohao.service

import cn.hellohao.model.entity.Config
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

@Service
interface ConfigService : IService<Config?> {
 fun getSourceType(): Config?
    fun setSourceType(config: Config?): Int?
}