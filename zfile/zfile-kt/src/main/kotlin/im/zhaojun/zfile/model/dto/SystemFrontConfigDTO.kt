package im.zhaojun.zfile.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import lombok.ToString

/**
 * 系统设置传输类
 * @author zhaojun
 */
@ToString
@Data
class SystemFrontConfigDTO {
    @JsonIgnore
    var  id: Int? = null
    var  siteName: String? = null
    var  searchEnable: Boolean? = null
    var  username: String? = null
    var  domain: String? = null
    var  customJs: String? = null
    var  customCss: String? = null
    var  tableSize: String? = null
    var  showOperator: Boolean? = null
    var  showDocument: Boolean? = null
    var  announcement: String? = null
    var  showAnnouncement: Boolean? = null
    var  layout: String? = null
    var  readme: String? = null
    var  debugMode: Boolean? = null
    var  defaultSwitchToImgMode: Boolean? = null
    var  showLinkBtn: Boolean? = null
    var  showShortLink: Boolean? = null
    var  showPathLink: Boolean? = null
    var  directLinkPrefix: String? = null
}