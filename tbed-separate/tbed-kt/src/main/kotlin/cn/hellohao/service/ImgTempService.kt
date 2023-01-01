package cn.hellohao.service

import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgTemp
import org.springframework.stereotype.Service

/**
 *
 */
@Service
interface ImgTempService {
    fun selectDelImgUidList(datatime: String?): List<Images?>?
    fun delImgAndExp(imguid: String?): Int?
    fun insertImgExp(imgDataExp: ImgTemp?): Int?
}