package im.zhaojun.zfile.model.entity

import lombok.Data
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @author zhaojun
 */
@Entity(name = "FILTER_CONFIG")
@Data
class FilterConfig {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    var  id: String? = null
    var  driveId: String? = null
    var  expression: String? = null
}