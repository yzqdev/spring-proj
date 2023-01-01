package cn.hellohao.model.dto;

import lombok.Data;

/**
 * @author yanni
 * @date time 2021/11/19 22:49
 * @modified By:
 */
@Data
public class UserSearchDto {
    private Integer pageNum;
    private Integer pageSize;
    private String queryText;
}
