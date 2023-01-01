package cn.hellohao.mapper

import cn.hellohao.model.dto.AlbumDto
import cn.hellohao.model.entity.Album
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
interface AlbumMapper : BaseMapper<Album?> {
    fun selectAlbum(albumDto: AlbumDto?): Album?
    fun selectAlbumURLList(@Param("page") page: Page<Album?>?, @Param("albumDto") albumDto: AlbumDto?): Page<Album?>?
    fun selectAlbumCount(@Param("userId") userId: String?): Int?
}