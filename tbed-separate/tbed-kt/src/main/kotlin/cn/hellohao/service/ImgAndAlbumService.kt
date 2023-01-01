package cn.hellohao.service

import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgAndAlbum
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:29
 */
@Service
interface ImgAndAlbumService : IService<ImgAndAlbum?> {
    fun deleteImgAndAlbum(imgname: String?): Int
    fun selectImgForAlbumkey(page: Page<Images?>?, albumkey: String?): Page<Images?>?
}