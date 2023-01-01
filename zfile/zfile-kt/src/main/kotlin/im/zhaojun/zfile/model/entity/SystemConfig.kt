package im.zhaojun.zfile.model.entity

import lombok.Data
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @author zhaojun
 */
@Entity(name = "SYSTEM_CONFIG")
@Data
class SystemConfig {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    var  id: String? = null

    @Column(name = "k")
    var  key: String? = null

    //@Lob
    var  value: String? = null
    var  remark: String? = null
}