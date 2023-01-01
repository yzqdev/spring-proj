package im.zhaojun.zfile.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author Zhao Jun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class StorageStrategyDTO {
    var  key: String? = null
    var  description: String? = null

    @JsonProperty(defaultValue = "false")
    var  available: Boolean? = null
}