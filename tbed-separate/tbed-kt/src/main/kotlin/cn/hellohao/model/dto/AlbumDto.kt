package cn.hellohao.model.dto

import cn.hellohao.model.entity.Images
import lombok.Data

/**
 * @author yanni
 * @date time 2021/11/19 20:40
 * @modified By:
 */
@Data
class AlbumDto {
    var  albumTitle: String? = null
    var  password: String? = null
    var  albumList: List<Images>? = null
    var  pageNum: Int? =0
    var  pageSize: Int? =10
    var  albumKey: String? = null

    /**
     * 用户id
     */
    var  userId: String? = null

    /**
     * 用户名
     */
    var  username: String? = null
}