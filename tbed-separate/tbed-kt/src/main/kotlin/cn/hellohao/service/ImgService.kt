package cn.hellohao.service

import cn.hellohao.model.dto.HomeImgDto
import cn.hellohao.model.dto.ImgSearchDto
import cn.hellohao.model.entity.Images
import cn.hellohao.model.vo.ImageVo
import cn.hellohao.model.vo.RecentUserVo
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service

/**
 * img服务
 *
 * @author yanni
 * @date 2021/11/20
 */
@Service
interface ImgService : IService<Images?> {
    /**
     * 选择图片
     *
     * @param imgSearchDto img搜索dto
     * @return [List]<[Images]>
     */
    fun selectImages(imgSearchDto: ImgSearchDto?): List<Images?>
    fun deleteImgById(id: String?): Int
    fun deleimgForImgUid(imguid: String?): Int
    fun countimg(userid: String?): Int
    fun selectByPrimaryKey(id: String?): Images
    fun counts(userid: String?): Int
    fun setImg(images: Images?): Int
    fun deleimgname(imgname: String?): Int
    fun deleall(id: String?): Int
    fun gettimeimg(time: String?): List<Images?>
    fun getUserMemory(userid: String?): Long
    fun getSourceMemory(source: String?): Long
    fun md5Count(images: Images?): Int
    fun selectImgUrlByMD5(md5key: String?): Images
    fun recentlyUploaded(userid: String?): List<Images?>
    fun recentlyUser(): List<RecentUserVo?>
    fun getyyyy(useriVo: String?): List<String?>
    fun countByM(images: HomeImgDto?): List<ImageVo?>
    fun selectImgUrlByImgUID(imguid: String?): Images
}