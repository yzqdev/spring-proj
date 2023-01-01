package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储键
 *
 * @author yanni
 * @date 2021/11/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageKey {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String accessKey;
    private String accessSecret;
    private String endpoint;
    private String bucketName;
    private String requestAddress;
    private Integer storageType;
    private String keyName;

}
