package cn.hellohao.mapper

import cn.hellohao.model.entity.UploadConfig
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 * 上传配置
 *
 * @author yanni
 * @date 2022/04/02
 */
@Mapper
interface UploadConfigMapper : BaseMapper<UploadConfig?> {
    /**
     * 得到更新配置
     *
     * @return [UploadConfig]
     */
    fun getUpdateConfig(): UploadConfig?

    /**
     * 设置更新配置
     *
     * @param uploadConfig 上传配置
     * @return [Integer]
     */
    fun setUpdateConfig(uploadConfig: UploadConfig?): Int?
}