package cn.hellohao.service.impl;

import cn.hellohao.mapper.AlbumMapper;
import cn.hellohao.mapper.ConfigMapper;
import cn.hellohao.mapper.ImgAndAlbumMapper;
import cn.hellohao.mapper.ImgMapper;
import cn.hellohao.model.dto.AlbumDto;
import cn.hellohao.exception.CodeException;
import cn.hellohao.model.entity.Album;
import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.ImgAndAlbum;
import cn.hellohao.service.AlbumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:30
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {
    @Resource
    AlbumMapper albumMapper;
    @Resource
    ImgAndAlbumMapper andAlbumMapper;
    @Resource
    ConfigMapper configMapper;
    @Resource
    ImgMapper imgMapper;


    @Override
    public Page<Images> getAlbumList(String[] array) {

        List<Images> images= new ArrayList<>(List.of());
        for (String s : array) {
            images.add(imgMapper.selectOne(new LambdaQueryWrapper<Images>().eq(Images::getImgUid, s)));
        }
        Page<Images> page=new Page<>(1,5);
          //List<Images> images= imgMapper.selectImageData( imgSearchDto);
        return page.setRecords(images);
    }

    @Override
    public Album selectAlbum(AlbumDto album) {

        return albumMapper.selectOne(new LambdaQueryWrapper<Album>().eq(Album::getAlbumKey, album.getAlbumKey()) );

    }

    @Override
    public Integer addAlbum(Album album) {
        return albumMapper.insert(album);
    }
@Override
    @Transactional
    public Integer addAlbumForImgAndAlbumMapper(ImgAndAlbum imgAndAlbum) {
        int tem;
        int r2 = andAlbumMapper.insert(imgAndAlbum);
        if (r2 > 0) {
            tem = 1;
        } else {
            throw new CodeException("插入画廊数据失败，回滚");
        }
        return tem;
    }

    @Override
    public Integer deleteAlbum(String albumkey) {
        andAlbumMapper.delete(new LambdaQueryWrapper<ImgAndAlbum>().eq(ImgAndAlbum::getAlbumKey, albumkey));
        return albumMapper.delete(new LambdaQueryWrapper<Album>().eq(Album::getAlbumKey, albumkey));
    }

    @Override
    public Page<Album> selectAlbumURLList(Page<Album> page,AlbumDto album) {
        return albumMapper.selectAlbumURLList(page,album);
    }

    @Override
    public Integer selectAlbumCount(String userid) {
        //return Math.toIntExact(albumMapper.selectCount(new LambdaQueryWrapper<Album>().eq(Album::getUserid, userid)));
        return albumMapper.selectAlbumCount(userid);
    }

    @Transactional
    public Integer delete(String albumkey) {
        Integer ret1 = albumMapper.delete(new LambdaQueryWrapper<Album>().eq(Album::getAlbumKey, albumkey));
        if (ret1 > 0) {
            ret1 = andAlbumMapper.delete(new LambdaQueryWrapper<ImgAndAlbum>().eq(ImgAndAlbum::getAlbumKey, albumkey));
        } else {
            throw new CodeException("删除画廊失败。");
        }
        return ret1;
    }
@Override
    @Transactional
    public Integer deleteAll(String[] albumkeyArr) {
        int ret1 = 0;
        for (String s : albumkeyArr) {
            ret1 = albumMapper.delete(new LambdaQueryWrapper<Album>().eq(Album::getAlbumKey, s));
            if (ret1 > 0) {
                ret1 = andAlbumMapper.delete(new LambdaQueryWrapper<ImgAndAlbum>().eq(ImgAndAlbum::getAlbumKey, s));
            } else {
                throw new CodeException("删除画廊失败。");
            }
        }
        return ret1;
    }
}
