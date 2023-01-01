package cn.hellohao.service.impl

import cn.hellohao.mapper.ImgTempMapper
import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgTemp
import cn.hellohao.service.ImgTempService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 */
@Service
class ImgTempServiceImpl : ImgTempService {
    @Autowired
    var imgDataExpMapper: ImgTempMapper? = null
    override fun selectDelImgUidList(datatime: String?): List<Images?>? {
        return imgDataExpMapper!!.selectDelImgUidList(datatime)
    }

    override fun delImgAndExp(imguid: String?): Int? {
        return imgDataExpMapper!!.delImgAndExp(imguid)
    }

    override fun insertImgExp(imgDataExp: ImgTemp?): Int? {
        return imgDataExpMapper!!.insertImgExp(imgDataExp)
    }
}