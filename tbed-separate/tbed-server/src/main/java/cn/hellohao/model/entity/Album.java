package cn.hellohao.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album {
    /**
     * albumkey
     */
    private String albumKey;
    /**
     * 专辑名称
     */
    private String albumTitle;
    /**
     * 创建日期
     */
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户标识
     */
    private String userId;
    private String username;




}
