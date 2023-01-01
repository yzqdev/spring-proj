package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.sql.Timestamp

/**
 * By Hellohao
 *
 * @TableName ImgTemp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class ImgTemp {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null

    /**
     *
     */
    var  imgUid: String? = null

    /**
     *
     */
    var  delTime: Timestamp? = null
}