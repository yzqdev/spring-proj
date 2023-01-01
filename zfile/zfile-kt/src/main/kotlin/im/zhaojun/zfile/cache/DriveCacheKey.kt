package im.zhaojun.zfile.cache

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author zhaojun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class DriveCacheKey {
    var  driveId: String? = null
    var  key: String? = null
}