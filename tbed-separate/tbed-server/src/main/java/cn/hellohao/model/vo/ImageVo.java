package cn.hellohao.model.vo;

import cn.hellohao.model.entity.Images;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yanni
 * @date time 2021/11/20 1:39
 * @modified By:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageVo extends Images {
    private Long countNum;
    private Integer monthNum;
    private String bucketName;
}
