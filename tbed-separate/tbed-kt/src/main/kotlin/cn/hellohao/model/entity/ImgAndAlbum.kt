package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ImgAndAlbum {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null

    /**
     * img的名字
     */
    var  imgName: String? = null
    var  albumKey: String? = null
    var  notes: String? = null
}