package cn.hellohao.model.entity

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021-10-22 11:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ReturnImage {
    var  uid: String? = null
    var  code: String? = null
    var  imgUrl: String? = null
    var  imgName: String? = null
    var  imgSize: Long? = null
}