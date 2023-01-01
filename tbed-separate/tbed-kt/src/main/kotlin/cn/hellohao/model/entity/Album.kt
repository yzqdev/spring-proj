package cn.hellohao.model.entity

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Album {
    /**
     * albumkey
     */
    var  albumKey: String? = null

    /**
     * 专辑名称
     */
    var  albumTitle: String? = null

    /**
     * 创建日期
     */
    var  createTime: LocalDateTime? = null
    var  updateTime: LocalDateTime? = null

    /**
     * 密码
     */
    var  password: String? = null

    /**
     * 用户标识
     */
    var  userId: String? = null
    var  username: String? = null
}