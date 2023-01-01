package im.zhaojun.zfile.model.entity

import im.zhaojun.zfile.model.enums.StorageTypeEnum
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * 驱动器
 *
 * @author zhaojun
 */
@Entity(name = "driver_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class DriveConfig {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
     var id: String? = null
    var enable: Boolean? = null
     var name: String? = null
     var enableCache: Boolean? = null
     var autoRefreshCache: Boolean? = null
     var type: StorageTypeEnum? = null
     var searchEnable: Boolean? = null
     var searchIgnoreCase: Boolean? = null
     var searchContainEncryptedFile: Boolean? = null
     var orderNum: Int? = null
     var defaultSwitchToImgMode: Boolean? = null
}