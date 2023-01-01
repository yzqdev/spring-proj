package im.zhaojun.zfile.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "SHORT_LINK")
@Data
public class ShortLinkConfig {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "im.zhaojun.zfile.util.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "`key`")
    private String key;

    private String url;

    private Date createDate;

}
