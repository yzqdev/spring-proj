package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class UserGroup {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null
    var  userId: String? = null
    var  groupId: String? = null
}