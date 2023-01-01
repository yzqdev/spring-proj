package im.zhaojun.zfile.model.dto

import im.zhaojun.zfile.model.entity.DriveConfig
import lombok.AllArgsConstructor
import lombok.Data

/**
 * @author Zhao Jun
 * 2021/5/26 15:17
 */
@Data
@AllArgsConstructor
class DriveListDTO {
    var  driveList: List<DriveConfig>? = null
    var  isInstall: Boolean? = null
}