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
 * @date 2021/10/26 18:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class SiteGroup {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null
    var  groupName: String? = null
    var  keyID: String? = null
    var  userType: Int? = null
    var  compress: Int? = null
    var  storageType: Int? = null
    var  keyName: String? = null
}