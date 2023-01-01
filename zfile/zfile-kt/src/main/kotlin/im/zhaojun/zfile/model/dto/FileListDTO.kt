package im.zhaojun.zfile.model.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
class FileListDTO {
    var  files: List<FileItemDTO>? = null
    var  config: SystemFrontConfigDTO? = null
}