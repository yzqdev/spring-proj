package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/10/26 18:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteGroup {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String groupName;
    private String keyID;
    private Integer userType;
    private Integer compress;
    private Integer storageType;
    private String keyName;


}
