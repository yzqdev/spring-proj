package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_config")
class SysConfig {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null
    var  register: Int? = null
    var  checkduplicate: String? = null
}