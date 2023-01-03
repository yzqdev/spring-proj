package org.javaboy.vhr.model

import com.baomidou.mybatisplus.annotation.IdType
import lombok.Builder
import lombok.Data
import java.io.Serializable

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Menu : Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private val id: String? = null
    private val url: String? = null
    private val path: String? = null
    private val component: String? = null
    private val name: String? = null
    private val iconCls: String? = null
    private val meta: Meta? = null
    private val parentId: Int? = null
    private val enabled: Boolean? = null
    private val children: List<Menu>? = null
    private val roles: List<Role>? = null
}