package cn.hellohao.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021-10-22 11:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnImage {
    private String uid;
    private String code;
    private String imgUrl;
    private String imgName;
    private Long imgSize;




}
