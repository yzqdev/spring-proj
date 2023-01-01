package cn.hellohao.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * By Hellohao
 *
 * @TableName ImgTemp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ImgTemp {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *
     */
    private String imgUid;


    /**
     *
     */
    private Timestamp delTime;


}