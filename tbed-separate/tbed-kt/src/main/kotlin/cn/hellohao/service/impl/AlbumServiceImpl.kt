package cn.hellohao.service.impl

import cn.hellohao.exception.CodeException
import cn.hellohao.mapper.AlbumMapper
import cn.hellohao.mapper.ConfigMapper
import cn.hellohao.mapper.ImgAndAlbumMapper
import cn.hellohao.mapper.ImgMapper
import cn.hellohao.model.dto.AlbumDto
import cn.hellohao.model.entity.Album
import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgAndAlbum
import cn.hellohao.service.AlbumService
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:30
 */
@Service
class AlbumServiceImpl(
    var albumMapper: AlbumMapper,
    var andAlbumMapper: ImgAndAlbumMapper,
    var configMapper: ConfigMapper,
    var imgMapper: ImgMapper
) : ServiceImpl<AlbumMapper?, Album?>(), AlbumService {


    override fun getAlbumList(array: Array<String?>): Page<Images>? {
        val images: MutableList<Images> = ArrayList(List.of())
        for (s in array) {
            images.add(
                imgMapper!!.selectOne(
                    LambdaQueryWrapper<Images>().eq(
                        SFunction<Images, Any> { obj: Images -> obj.imgUid },
                        s
                    )
                )
            )
        }
        val page = Page<Images>(1, 5)
        //List<Images> images= imgMapper.selectImageData( imgSearchDto);
        return page.setRecords(images)
    }

    override fun selectAlbum(album: AlbumDto): Album? {
        return albumMapper!!.selectOne(
            LambdaQueryWrapper<Album>().eq(
                SFunction<Album, Any> { obj: Album -> obj.albumKey },
                album.albumKey
            )
        )
    }

    override fun addAlbum(album: Album): Int {
        return albumMapper!!.insert(album)
    }

    @Transactional
    override fun addAlbumForImgAndAlbumMapper(imgAndAlbum: ImgAndAlbum): Int {
        val tem: Int
        val r2 = andAlbumMapper!!.insert(imgAndAlbum)
        tem = if (r2 > 0) {
            1
        } else {
            throw CodeException("插入画廊数据失败，回滚")
        }
        return tem
    }

    override fun deleteAlbum(albumkey: String?): Int {
        andAlbumMapper!!.delete(
            LambdaQueryWrapper<ImgAndAlbum>().eq(
                SFunction<ImgAndAlbum, Any> { obj: ImgAndAlbum -> obj.albumKey },
                albumkey
            )
        )
        return albumMapper!!.delete(
            LambdaQueryWrapper<Album>().eq(
                SFunction<Album, Any> { obj: Album -> obj.albumKey },
                albumkey
            )
        )
    }

    override fun selectAlbumURLList(page: Page<Album?>?, album: AlbumDto?): Page<Album?>? {
        return albumMapper!!.selectAlbumURLList(page, album)
    }

    override fun selectAlbumCount(userid: String?): Int? {
        //return Math.toIntExact(albumMapper.selectCount(new LambdaQueryWrapper<Album>().eq(Album::getUserid, userid)));
        return albumMapper!!.selectAlbumCount(userid)
    }

    @Transactional
    fun delete(albumkey: String?): Int {
        var ret1 = albumMapper!!.delete(
            LambdaQueryWrapper<Album>().eq(
                SFunction<Album, Any> { obj: Album -> obj.albumKey },
                albumkey
            )
        )
        ret1 = if (ret1 > 0) {
            andAlbumMapper!!.delete(
                LambdaQueryWrapper<ImgAndAlbum>().eq(
                    SFunction<ImgAndAlbum, Any> { obj: ImgAndAlbum -> obj.albumKey },
                    albumkey
                )
            )
        } else {
            throw CodeException("删除画廊失败。")
        }
        return ret1
    }

    @Transactional
    override fun deleteAll(albumkeyArr: Array<String?>): Int {
        var ret1 = 0
        for (s in albumkeyArr) {
            ret1 = albumMapper!!.delete(
                LambdaQueryWrapper<Album>().eq(
                    SFunction<Album, Any> { obj: Album -> obj.albumKey },
                    s
                )
            )
            ret1 = if (ret1 > 0) {
                andAlbumMapper!!.delete(
                    LambdaQueryWrapper<ImgAndAlbum>().eq(
                        SFunction<ImgAndAlbum, Any> { obj: ImgAndAlbum -> obj.albumKey },
                        s
                    )
                )
            } else {
                throw CodeException("删除画廊失败。")
            }
        }
        return ret1
    }
}