package cn.hellohao.service;


import java.util.List;

import cn.hellohao.model.entity.Images;
import cn.hellohao.model.dto.HomeImgDto;
import cn.hellohao.model.dto.ImgSearchDto;
import cn.hellohao.model.vo.ImageVo;
import cn.hellohao.model.vo.RecentUserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * img服务
 *
 * @author yanni
 * @date 2021/11/20
 */
@Service
public interface ImgService extends IService<Images> {
    /**
     * 选择图片
     *
     * @param imgSearchDto img搜索dto
     * @return {@link List}<{@link Images}>
     */
    List<Images> selectImages(ImgSearchDto  imgSearchDto);

    Integer deleteImgById(String id);

    Integer deleimgForImgUid(String imguid);

    Integer countimg(String userid);

    Images selectByPrimaryKey(String id);

    Integer counts(String userid);

    Integer setImg(Images images);

    Integer deleimgname(String imgname);

    Integer deleall(String id);

    List<Images> gettimeimg(String time);

    Long getUserMemory(String userid);

    Long getSourceMemory(String source);

    Integer md5Count(Images images);

    Images selectImgUrlByMD5(String md5key);

    List<Images> recentlyUploaded(String userid);

    List<RecentUserVo> recentlyUser();

    List<String> getyyyy(String useriVo);

    List<ImageVo> countByM(HomeImgDto images);

    Images selectImgUrlByImgUID( String imguid);

}
