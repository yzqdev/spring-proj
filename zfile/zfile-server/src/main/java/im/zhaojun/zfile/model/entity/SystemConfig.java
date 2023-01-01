package im.zhaojun.zfile.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author zhaojun
 */
@Entity(name = "SYSTEM_CONFIG")
@Data
public class SystemConfig {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "k")
    private String key;

    //@Lob
    private String value;

    private String remark;

}