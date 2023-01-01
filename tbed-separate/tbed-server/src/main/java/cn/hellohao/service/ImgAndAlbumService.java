package cn.hellohao.service;

import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.ImgAndAlbum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:29
 */
@Service
public interface ImgAndAlbumService extends IService<ImgAndAlbum> {


    Integer deleteImgAndAlbum(String imgname);



    Page<Images> selectImgForAlbumkey(Page<Images> page,String albumkey);
}
