package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Code {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String value;
    private String expandCode;

}
