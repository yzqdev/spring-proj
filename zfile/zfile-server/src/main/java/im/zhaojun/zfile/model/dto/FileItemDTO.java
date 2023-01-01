package im.zhaojun.zfile.model.dto;

import im.zhaojun.zfile.model.enums.FileTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaojun
 */

@Data
public class FileItemDTO implements Serializable {

    private String name;
    private Date time;
    private Long size;
    private FileTypeEnum type;
    private String path;
    private String src;
private String mimetype;
}
