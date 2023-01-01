package cn.hellohao.model.vo;

import lombok.Data;

/**
 * @author yanni
 * @date time 2021/11/20 14:59
 * @modified By:
 */
@Data
public class RecentUserVo {
    private String username;
    private Integer counts;
    private Integer userId;
}
