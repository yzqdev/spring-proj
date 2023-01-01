package cn.hellohao.model.dto

import lombok.Data
import lombok.experimental.Accessors

/**
 * @author yanni
 * @date time 2021/11/19 19:27
 * @modified By:
 */
@Accessors(chain = true)
@Data
class ImgSearchDto {
    var  pageNum: Int? = null
    var  pageSize: Int? = null
    var  username: String? = null
    var  source: Int? = null
    var  startTime: String? = null
    var  stopTime: String? = null
    var  selectType: Int? = null
    var  classifulids: Array<String>
    var  violation: Boolean? = null
    var  userId: String? = null
}