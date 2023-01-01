package cn.hellohao.model.dto

import lombok.Data

/**
 * @author yanni
 * @date time 2021/11/19 23:16
 * @modified By:
 */
@Data
class PageDto {
    var  pageNum: Int = 0
    var  pageSize: Int = 10
}