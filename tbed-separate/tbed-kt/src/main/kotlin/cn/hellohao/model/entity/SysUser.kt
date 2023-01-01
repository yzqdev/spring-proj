package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.extension.activerecord.Model
import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import java.time.LocalDateTime

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
open class SysUser : Model<SysUser?>() {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    var id: String? = null

    //@NotBlank(message = "用户名不能为空")
    // @Length(min = 6, max = 20, message = "用户名需要为 6 - 20 个字符")
    var username: String? = null

    /**
     * 密码
     */
    var password: String? = null

    /**
     * 电子邮件
     */
    var email: String? = null

    /**
     * 生日
     */
    var birthday: LocalDateTime? = null
    var level: Int? = null
    var uid: String? = null
    var isok: Int? = null

    /**
     * 内存
     */
    var memory: Long? = null

    /**
     * 组id
     */
    var groupId: String? = null

    /**
     * 组名称
     */
    var groupName: String? = null

    ///**
    // * 用户照片数量
    // */
    //private Long counts;
    var createTime: LocalDateTime? = null
    var updateTime: LocalDateTime? = null
}