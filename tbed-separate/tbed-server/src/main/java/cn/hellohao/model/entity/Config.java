package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 配置
 *
 * @author yanni
 * @date 2021/11/17
 */
@Data
@AllArgsConstructor
public class Config {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private Integer sourcekey;
    private Integer emails;
    private String webname;
    private String explain;
    private String video;
    private String links;
    private String notice;
    private String baidu;
    private String backtype;
    private String domain;
    private String background1;
    private String background2;
    private String webms;
    private String webkeywords;
    private String webfavicons;
    private String websubtitle;
    private String logo;
    private String aboutInfo;


}
