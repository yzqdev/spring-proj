package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * 上传配置
 *
 * @author yanni
 * @date 2021/11/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("upload_config")
class UploadConfig {
    var  suffix: String? = null
    var  fileSizeTourists: String? = null
    var  fileSizeUser: String? = null
    var  imgCountTourists: Int? = null
    var  imgCountUser: Int? = null

    /**
     * url类型
     */
    var  urlType: Int? = null
    var  isUpdate: Int? = null
    var  api: Int? = null

    /**
     * 访问存储
     */
    var  visitorStorage: String? = null

    /**
     * 用户存储
     */
    var  userStorage: Long? = null

    /**
     * 黑名单
     */
    var  blacklist: String? = null
    var  userclose: Int? = null
}