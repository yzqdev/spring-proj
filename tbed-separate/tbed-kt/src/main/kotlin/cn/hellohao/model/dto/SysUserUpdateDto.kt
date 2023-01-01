package cn.hellohao.model.dto

import cn.hellohao.model.entity.SysUser
import lombok.Data
import lombok.EqualsAndHashCode

/**
 * @author yanni
 * @date time 2021/11/19 23:01
 * @modified By:
 */
@EqualsAndHashCode(callSuper = true)
@Data
class SysUserUpdateDto : SysUser() {
    var  counts: Long? = null
    var  groupName: String? = null
}