package im.zhaojun.zfile.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.model.enums.StorageTypeEnumSerializerConvert
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * 系统设置传输类
 *
 * @author zhaojun
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SystemConfigDTO {
    @JsonIgnore
    var  id: Int? = null
    var  siteName: String? = null
    var  username: String? = null

    @JsonSerialize(using = StorageTypeEnumSerializerConvert::class)
    var  storageStrategy: StorageTypeEnum? = null

    @JsonIgnore
    var  password: String? = null
    var  domain: String? = null
    var  customJs: String? = null
    var  customCss: String? = null
    var  tableSize: String? = null
    var  showOperator: Boolean? = null
    var  showDocument: Boolean? = null
    var  announcement: String? = null
    var  showAnnouncement: Boolean? = null
    var  layout: String? = null
    var  showLinkBtn: Boolean? = null
    var  showShortLink: Boolean? = null
    var  showPathLink: Boolean? = null
}