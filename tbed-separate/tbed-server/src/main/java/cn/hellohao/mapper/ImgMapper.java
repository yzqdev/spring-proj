package cn.hellohao.mapper;

import java.util.List;

import cn.hellohao.model.dto.HomeImgDto;
import cn.hellohao.model.dto.ImgSearchDto;
import cn.hellohao.model.vo.ImageVo;
import cn.hellohao.model.vo.RecentUserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.model.entity.Images;

@Mapper
public interface ImgMapper extends BaseMapper<Images> {

  List<Images>  selectImageData(@Param("img") ImgSearchDto imgSearchDto);





    Integer deleimgForImgUid(@Param("imguid") String imguid);

    Images selectByPrimaryKey(@Param("id") String id);

    Integer counts(@Param("userId") String userId);

    Integer setImg(Images images);

    Integer deleimgname(@Param("imgname") String imgname);
    Integer deleall(@Param("id") String id);

    List<Images> getTimeImg(@Param("time") String time);

    Long getUserMemory(@Param("userid") String userid);

    Long getSourceMemory(@Param("source") String source);

    Integer md5Count(Images images);

  /**
   * 选择通过md5 img url
   *
   * @param md5key md5key
   * @return {@link Images}
   */
  Images selectImgUrlByMD5(@Param("md5key") String md5key);

  /**
   * 最近上传
   *
   * @param userId 用户id
   * @return {@link List}<{@link Images}>
   */
  List<Images> recentlyUploaded(@Param("userId") String userId);

  /**
   * 最近用户
   *
   * @return {@link List}<{@link RecentUserVo}>
   */
  List<RecentUserVo> recentlyUser();

    List<String> getyyyy(@Param("userId") String userId);

    List<ImageVo> countByM(HomeImgDto homeImgDto);


}
