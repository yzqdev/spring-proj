package cn.hellohao.service.impl

import cn.hellohao.mapper.UploadConfigMapper
import cn.hellohao.model.entity.UploadConfig
import cn.hellohao.service.UploadConfigService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

@Service
class UploadConfigServiceImpl(private val uploadConfigMapper: UploadConfigMapper) : ServiceImpl<UploadConfigMapper?, UploadConfig?>(), UploadConfigService {

    override fun getUpdateConfig(): UploadConfig? {
        return uploadConfigMapper.getUpdateConfig()
    }

    override fun setUpdateConfig(uploadConfig: UploadConfig?): Int? {
        return uploadConfigMapper!!.setUpdateConfig(uploadConfig)
    }
}