package im.zhaojun.zfile.model.entity;

import im.zhaojun.zfile.model.enums.StorageTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author zhaojun
 */
@Entity(name = "STORAGE_CONFIG")
@Data
@NoArgsConstructor
public class StorageConfig {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private StorageTypeEnum type;

    @Column(name = "k")
    private String key;

    private String title;

    //@Lob
    private String value;

    private String driveId;

    public StorageConfig(String key, String title) {
        this.key = key;
        this.title = title;
    }

}