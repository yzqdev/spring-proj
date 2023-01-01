package cn.hellohao.model.base;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yangzhengqian
 * @description
 * @date:Created time 2021/8/18 9:05
 * @modified By:
 */
@Data
@Slf4j
public class ImgUrls {

    private String imgUrl;
    private String imgTag;
    public ImgUrls(){
        log.info("hllow");
    }
}
