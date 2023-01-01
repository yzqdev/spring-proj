package cn.hellohao.service.impl;

import cn.hellohao.mapper.ImgAndAlbumMapper;
import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.ImgAndAlbum;
import cn.hellohao.service.ImgAndAlbumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/12/19 15:40
 */
@Service
public class ImgAndAlbumServiceImpl extends ServiceImpl<ImgAndAlbumMapper,ImgAndAlbum> implements ImgAndAlbumService {
    @Autowired
    ImgAndAlbumMapper imgAndAlbumMapper;




    @Override
    public Integer deleteImgAndAlbum(String imgname) {
        return imgAndAlbumMapper.delete(new LambdaQueryWrapper<ImgAndAlbum>().eq(ImgAndAlbum::getImgName,imgname));
    }



    @Override
    public Page<Images> selectImgForAlbumkey(Page<Images> page,String albumkey) {
        return imgAndAlbumMapper.selectImgForAlbumkey( page,albumkey);
    }
}
