package cn.hellohao.model.dto;

import cn.hellohao.model.entity.Images;
import lombok.Data;

import java.util.List;

/**
 * @author yanni
 * @date time 2021/11/19 20:40
 * @modified By:
 */
@Data
public class AlbumDto {
    private String albumTitle;
    private String password;
    private List<Images> albumList;
    private Integer pageNum;
    private Integer pageSize;
    private String albumKey;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
}
