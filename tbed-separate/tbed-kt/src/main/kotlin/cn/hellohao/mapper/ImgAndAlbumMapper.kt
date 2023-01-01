package cn.hellohao.mapper

import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgAndAlbum
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Mapper
interface ImgAndAlbumMapper : BaseMapper<ImgAndAlbum> {
    /**
     * 得到imgname专辑
     *
     * @param imgname imgname
     * @return [List]<[ImgAndAlbum]>
     */
    fun getAlbumForImgname(@Param("imgname") imgname: String): List<ImgAndAlbum>

    /**
     * 选择img albumkey
     *
     * @param page     页面
     * @param albumKey 专辑关键
     * @return [Page]<[Images]>
     */
    fun selectImgForAlbumkey(@Param("page") page: Page<Images>, @Param("albumKey") albumKey: String): Page<Images>
}