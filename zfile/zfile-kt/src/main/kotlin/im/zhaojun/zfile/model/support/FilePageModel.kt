package im.zhaojun.zfile.model.support

import im.zhaojun.zfile.model.dto.FileItemDTO
import lombok.AllArgsConstructor
import lombok.Data

/**
 * @author zhaojun
 */
@Data
@AllArgsConstructor
class FilePageModel {
    var  totalPage = 0
    var  fileList: List<FileItemDTO>? = null
}