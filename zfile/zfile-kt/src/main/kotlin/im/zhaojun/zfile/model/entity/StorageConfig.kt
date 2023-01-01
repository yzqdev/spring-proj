package im.zhaojun.zfile.model.entity

import im.zhaojun.zfile.model.enums.StorageTypeEnum
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @author zhaojun
 */
@Entity(name = "STORAGE_CONFIG")
@Data

class StorageConfig(@field:Column(name = "k") var  key: String, var  title: String) {



    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    var  id: String? = null
    var  type: StorageTypeEnum? = null

    //@Lob
    var  value: String? = null
    var  driveId: String? = null
}