package im.zhaojun.zfile.model.dto

import lombok.AllArgsConstructor
import lombok.Data

/**
 * @author zhaojun
 */
@Data
@AllArgsConstructor
class CacheInfoDTO {
    var  cacheCount: Int? = null
    var  hitCount: Int? = null
    var  missCount: Int? = null
    var  cacheKeys: Set<String>? = null
}