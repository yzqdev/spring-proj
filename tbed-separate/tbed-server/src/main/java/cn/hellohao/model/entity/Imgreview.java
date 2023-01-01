package cn.hellohao.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Imgreview {
    private String id;

    private String appId;

    private String apiKey;

    private String secretKey;

    private Integer using;

    private Integer count;

   

}