package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * 存储键
 *
 * @author yanni
 * @date 2021/11/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class StorageKey {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null
    var  accessKey: String? = null
    var  accessSecret: String? = null
    var  endpoint: String? = null
    var  bucketName: String? = null
    var  requestAddress: String? = null
    var  storageType: Int? = null
    var  keyName: String? = null
}