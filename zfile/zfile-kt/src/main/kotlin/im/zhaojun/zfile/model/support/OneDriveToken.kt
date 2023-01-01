package im.zhaojun.zfile.model.support

import com.alibaba.fastjson.annotation.JSONField
import lombok.Data

/**
 * @author zhaojun
 */
@Data
class OneDriveToken {
    @JSONField(name = "access_token")
    var  accessToken: String? = null

    @JSONField(name = "refresh_token")
    var  refreshToken: String? = null
}