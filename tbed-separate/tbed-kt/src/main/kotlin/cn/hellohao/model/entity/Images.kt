package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

/**
 * 图片
 *
 * @author yanni
 * @date 2021/11/17
 */
@Data
@NoArgsConstructor
@TableName("img_data")
@AllArgsConstructor
@Builder
open class Images {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null

    /**
     * imgname
     */
    var  imgName: String? = null

    /**
     * imgurl
     */
    var  imgUrl: String? = null
    var  userId: String? = null

    /**
     * 大小
     */
    var  sizes: Int? = null

    /**
     * 不正常的
     */
    var  abnormal: String? = null

    /**
     * 源
     */
    var  source: String? = null

    /**
     * imgtype
     */
    var  imgType: Int? = null

    /**
     * 更新时间
     */
    var  updateTime: LocalDateTime? = null
    var  createTime: LocalDateTime? = null
    /**
     * 存储类型
     */
    //private Integer storageType;
    /**
     * 开始时间
     */
    var  explains: String? = null

    /**
     * md5key
     */
    var  md5key: String? = null
    var  notes: String? = null
    var  imgUid: String? = null
    var  format: String? = null
    var  about: String? = null
    var  violation: String? = null //private String albumTitle;
    //@Length(min = 0, max = 10, message = "画廊密码不能超过10个字符")
    //private String password;
    //private Integer selectType;
    //private String yyyy;
    //private String[] classifuidlist; //类别uid集合
    //private String classificationuid; //类别uid集合
}