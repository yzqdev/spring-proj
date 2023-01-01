package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
public class Images {

@TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * imgname
     */
    private String imgName;
    /**
     * imgurl
     */
    private String imgUrl;
    private String userId;
    /**
     * 大小
     */
    private Integer sizes;
    /**
     * 不正常的
     */
    private String abnormal;
    /**
     * 源
     */
    private String source;
    /**
     * imgtype
     */
    private Integer imgType;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    private LocalDateTime createTime;


    /**
     * 存储类型
     */
    //private Integer storageType;
    /**
     * 开始时间
     */

    private String explains;
    /**
     * md5key
     */
    private String md5key;
    private String notes;
    private String imgUid;
    private String format;
    private String about;

    private String violation;
    //private String albumTitle;
    //@Length(min = 0, max = 10, message = "画廊密码不能超过10个字符")
    //private String password;
    //private Integer selectType;

    //private String yyyy;
    //private String[] classifuidlist; //类别uid集合
    //private String classificationuid; //类别uid集合







}
	
		

