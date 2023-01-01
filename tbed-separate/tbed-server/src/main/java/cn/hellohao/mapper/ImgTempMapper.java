package cn.hellohao.mapper;

import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.ImgTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @Entity dao.pojo.Imgdataexp
*/
@Mapper
public interface ImgTempMapper extends BaseMapper<ImgTemp> {

    List<Images> selectDelImgUidList(@Param("datatime") String datatime);
    Integer delImgAndExp(@Param("imgUid") String imgUid);
    Integer insertImgExp(ImgTemp imgDataExp);

}
