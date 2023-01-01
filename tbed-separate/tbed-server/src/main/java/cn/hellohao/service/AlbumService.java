package cn.hellohao.service;

import cn.hellohao.model.entity.Album;
import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.ImgAndAlbum;
import cn.hellohao.model.dto.AlbumDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Service
public interface AlbumService extends IService<Album> {

    Page<Images> getAlbumList(String[] array);

    Album selectAlbum(AlbumDto albumDto);

    Integer addAlbum(Album  album );

    Integer deleteAlbum(String albumkey);

    Page<Album> selectAlbumURLList(Page<Album> page,AlbumDto albumDto);

    Integer selectAlbumCount(String userid);
    Integer addAlbumForImgAndAlbumMapper(ImgAndAlbum imgAndAlbum);
   Integer deleteAll(String[] albumkeyArr);
}
