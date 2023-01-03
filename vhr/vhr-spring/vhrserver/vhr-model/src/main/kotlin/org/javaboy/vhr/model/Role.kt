package org.javaboy.vhr.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Role : Serializable {
    private val id: Int? = null
    private val name: String? = null
    private val nameZh: String? = null
}