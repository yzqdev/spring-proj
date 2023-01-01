package cn.hellohao.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author yanni
 * @date 2021/11/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends Model<SysUser> {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    //@NotBlank(message = "用户名不能为空")
    // @Length(min = 6, max = 20, message = "用户名需要为 6 - 20 个字符")
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 生日
     */
    private LocalDateTime birthday;
    private Integer level;
    private String uid;
    private Integer isok;
    /**
     * 内存
     */
    private Long memory;
    /**
     * 组id
     */
    private String groupId;
    /**
     * 组名称
     */
    private String groupName;
    ///**
    // * 用户照片数量
    // */
    //private Long counts;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
