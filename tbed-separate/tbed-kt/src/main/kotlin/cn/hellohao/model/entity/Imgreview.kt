package cn.hellohao.model.entity

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Imgreview {
    var  id: String? = null
    var  appId: String? = null
    var  apiKey: String? = null
    var  secretKey: String? = null
    var  using: Int? = null
    var  count: Int? = null
}