package cn.hellohao.service.impl

import cn.hellohao.mapper.ImgAndAlbumMapper
import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgAndAlbum
import cn.hellohao.service.ImgAndAlbumService
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/12/19 15:40
 */
@Service
class ImgAndAlbumServiceImpl( var imgAndAlbumMapper: ImgAndAlbumMapper) : ServiceImpl<ImgAndAlbumMapper?, ImgAndAlbum?>(), ImgAndAlbumService {

    override fun deleteImgAndAlbum(imgname: String?): Int {
        return imgAndAlbumMapper!!.delete(
            LambdaQueryWrapper<ImgAndAlbum>().eq(
                SFunction<ImgAndAlbum, Any> { obj: ImgAndAlbum -> obj.imgName },
                imgname
            )
        )
    }

    override fun selectImgForAlbumkey(page: Page<Images>, albumkey: String): Page<Images> {
        return imgAndAlbumMapper.selectImgForAlbumkey(page, albumkey)
    }
}