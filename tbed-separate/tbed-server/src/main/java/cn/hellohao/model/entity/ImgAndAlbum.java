package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImgAndAlbum {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * img的名字
     */
    private String imgName;
    private String albumKey;
    private String notes;


}
