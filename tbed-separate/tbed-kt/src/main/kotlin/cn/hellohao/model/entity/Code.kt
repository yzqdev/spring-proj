package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Code {
    @TableId(type = IdType.ASSIGN_UUID)
    var  id: String? = null
    var  value: String? = null
    var  expandCode: String? = null
}