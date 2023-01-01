package cn.hellohao.mapper;

import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.ImgAndAlbum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Mapper
public interface ImgAndAlbumMapper extends BaseMapper<ImgAndAlbum> {


    /**
     * 得到imgname专辑
     *
     * @param imgname imgname
     * @return {@link List}<{@link ImgAndAlbum}>
     */
    List<ImgAndAlbum> getAlbumForImgname(@Param("imgname") String imgname);


    /**
     * 选择img albumkey
     *
     * @param page     页面
     * @param albumKey 专辑关键
     * @return {@link Page}<{@link Images}>
     */
    Page<Images> selectImgForAlbumkey(@Param("page")Page<Images> page,@Param("albumKey") String albumKey);
}
