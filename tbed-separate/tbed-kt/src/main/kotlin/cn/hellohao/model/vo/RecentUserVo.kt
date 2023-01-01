package cn.hellohao.model.vo

import lombok.Data

/**
 * @author yanni
 * @date time 2021/11/20 14:59
 * @modified By:
 */
@Data
class RecentUserVo {
    var  username: String? = null
    var  counts: Int? = null
    var  userId: Int? = null
}