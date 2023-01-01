package im.zhaojun.zfile.model.dto

import lombok.Data

/**
 * @author zhaojun
 */
@Data
class StorageStrategyConfig {
    var  endPoint: String? = null
    var  pathStyle: String? = null
    var  isPrivate: Boolean? = null
    var  accessKey: String? = null
    var  secretKey: String? = null
    var  bucketName: String? = null
    var  host: String? = null
    var  port: String? = null
    var  accessToken: String? = null
    var  refreshToken: String? = null
    var  secretId: String? = null
    var  filePath: String? = null
    var  username: String? = null
    var  password: String? = null
    var  domain: String? = null
    var  basePath: String? = null
    var  siteId: String? = null
    var  siteName: String? = null
    var  siteType: String? = null
    var  proxyDomain: String? = null
    var  region: String? = null
}