package cn.hellohao.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/21 9:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Domain {
    private Integer id;
    private String domain;
    private String code;

}
