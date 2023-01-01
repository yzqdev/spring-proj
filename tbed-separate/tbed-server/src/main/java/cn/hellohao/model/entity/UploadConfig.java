package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传配置
 *
 * @author yanni
 * @date 2021/11/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("upload_config")
public class UploadConfig {
    private String suffix;
    private String fileSizeTourists;
    private String fileSizeUser;
    private Integer imgCountTourists;
    private Integer imgCountUser;
    /**
     * url类型
     */
    private Integer urlType;
    private Integer isUpdate;
    private Integer api;
    /**
     * 访问存储
     */
    private String visitorStorage;
    /**
     * 用户存储
     */
    private Long userStorage;
    /**
     * 黑名单
     */
    private String blacklist;
    private Integer userclose;


}
