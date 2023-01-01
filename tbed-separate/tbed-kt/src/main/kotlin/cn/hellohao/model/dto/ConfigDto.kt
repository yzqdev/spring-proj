package cn.hellohao.model.dto

import cn.hellohao.model.entity.Config
import cn.hellohao.model.entity.SysConfig
import cn.hellohao.model.entity.UploadConfig
import lombok.Data

/**
 * @author yanni
 * @date time 2021/11/20 1:00
 * @modified By:
 */
@Data
class ConfigDto {
    var  uploadConfig: UploadConfig? = null
    var  sysConfig: SysConfig? = null
    var  config: Config? = null
}