package cn.hellohao.model.entity

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/21 9:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class Domain {
    var  id: Int? = null
    var  domain: String? = null
    var  code: String? = null
}