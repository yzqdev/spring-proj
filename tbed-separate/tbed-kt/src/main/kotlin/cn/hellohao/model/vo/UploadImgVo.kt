package cn.hellohao.model.vo

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author yanni
 * @date time 2022/5/16 9:42
 * @modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class UploadImgVo {
    var  url: String? = null
    var  name: String? = null
    var  imgUid: String? = null
}