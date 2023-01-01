package im.zhaojun.zfile.model.dto

import lombok.Data

@Data
class SharePointInfoVO {
    var  type: String? = null
    var  accessToken: String? = null
    var  domainPrefix: String? = null
    var  siteType: String? = null
    var  siteName: String? = null
    var  domainType: String? = null
}