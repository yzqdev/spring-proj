package cn.hellohao.service

import cn.hellohao.model.entity.UploadConfig
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

@Service
interface UploadConfigService : IService<UploadConfig?> {
    fun getUpdateConfig(): UploadConfig?
    fun setUpdateConfig(updateConfig: UploadConfig?): Int?
}