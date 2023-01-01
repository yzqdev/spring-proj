package cn.hellohao.auth.token

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author yanni
 * @date time 2022/6/6 13:54
 * @modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

class UserClaim {
    var check: Boolean? = null
    var email: String? = null
    var password: String? = null
    var uid: String? = null
}