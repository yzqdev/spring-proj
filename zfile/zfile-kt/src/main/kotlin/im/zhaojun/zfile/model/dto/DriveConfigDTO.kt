package im.zhaojun.zfile.model.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.model.enums.StorageTypeEnumJsonDeSerializerConvert
import lombok.Data

/**
 * @author zhaojun
 */
@Data
class DriveConfigDTO {
    var  id: String? = null
    var  name: String? = null

    @JsonDeserialize(using = StorageTypeEnumJsonDeSerializerConvert::class)
    var  type: StorageTypeEnum? = null
    var  enable: Boolean? = null
    var  enableCache = false
    var  autoRefreshCache = false
    var  searchEnable = false
    var  searchIgnoreCase = false
    var  searchContainEncryptedFile = false
    var  orderNum: Int? = null
    var  storageStrategyConfig: StorageStrategyConfig? = null
    var  defaultSwitchToImgMode = false
}