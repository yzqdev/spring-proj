package im.zhaojun.zfile.model.entity

import lombok.Data
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "SHORT_LINK")
@Data
class ShortLinkConfig {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    var  id: String? = null

    @Column(name = "`key`")
    var  key: String? = null
    var  url: String? = null
    var  createDate: Date? = null
}