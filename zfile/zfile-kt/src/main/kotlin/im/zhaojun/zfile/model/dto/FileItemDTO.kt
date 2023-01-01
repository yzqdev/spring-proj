package im.zhaojun.zfile.model.dto

import im.zhaojun.zfile.model.enums.FileTypeEnum
import lombok.Data
import java.io.Serializable
import java.util.*

/**
 * @author zhaojun
 */
@Data
class FileItemDTO : Serializable {
    var  name: String? = null
    var  time: Date? = null
    var  size: Long? = null
    var  type: FileTypeEnum? = null
    var  path: String? = null
    var  src: String? = null
    var  mimetype: String? = null
}