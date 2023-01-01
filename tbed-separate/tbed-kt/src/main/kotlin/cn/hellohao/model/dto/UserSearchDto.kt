package cn.hellohao.model.dto

import lombok.Data

/**
 * @author yanni
 * @date time 2021/11/19 22:49
 * @modified By:
 */
@Data
class UserSearchDto {
    var  pageNum: Int = 0
    var  pageSize: Int =10
    var  queryText: String = ""
}