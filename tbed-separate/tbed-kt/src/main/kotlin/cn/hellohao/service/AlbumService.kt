package cn.hellohao.service

import cn.hellohao.model.dto.AlbumDto
import cn.hellohao.model.entity.Album
import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgAndAlbum
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Service
interface AlbumService : IService<Album?> {
    fun getAlbumList(array: Array<String?>): Page<Images>?
    fun selectAlbum(albumDto: AlbumDto): Album?
    fun addAlbum(album: Album): Int
    fun deleteAlbum(albumkey: String?): Int
    fun selectAlbumURLList(page: Page<Album?>?, albumDto: AlbumDto?): Page<Album?>?
    fun selectAlbumCount(userid: String?): Int?
    fun addAlbumForImgAndAlbumMapper(imgAndAlbum: ImgAndAlbum): Int
    fun deleteAll(albumkeyArr: Array<String?>): Int
}