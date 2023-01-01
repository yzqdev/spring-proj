package cn.hellohao.mapper

import cn.hellohao.model.dto.HomeImgDto
import cn.hellohao.model.dto.ImgSearchDto
import cn.hellohao.model.entity.Images
import cn.hellohao.model.vo.ImageVo
import cn.hellohao.model.vo.RecentUserVo
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ImgMapper : BaseMapper<Images?> {
    fun selectImageData(@Param("img") imgSearchDto: ImgSearchDto?): List<Images?>?
    fun deleimgForImgUid(@Param("imguid") imguid: String?): Int?
    fun selectByPrimaryKey(@Param("id") id: String?): Images?
    fun counts(@Param("userId") userId: String?): Int?
    fun setImg(images: Images?): Int?
    fun deleimgname(@Param("imgname") imgname: String?): Int?
    fun deleall(@Param("id") id: String?): Int?
    fun getTimeImg(@Param("time") time: String?): List<Images?>?
    fun getUserMemory(@Param("userid") userid: String?): Long?
    fun getSourceMemory(@Param("source") source: String?): Long?
    fun md5Count(images: Images?): Int?

    /**
     * 选择通过md5 img url
     *
     * @param md5key md5key
     * @return [Images]
     */
    fun selectImgUrlByMD5(@Param("md5key") md5key: String?): Images?

    /**
     * 最近上传
     *
     * @param userId 用户id
     * @return [List]<[Images]>
     */
    fun recentlyUploaded(@Param("userId") userId: String?): List<Images?>?

    /**
     * 最近用户
     *
     * @return [List]<[RecentUserVo]>
     */
    fun recentlyUser(): List<RecentUserVo?>?
    fun getyyyy(@Param("userId") userId: String?): List<String?>?
    fun countByM(homeImgDto: HomeImgDto?): List<ImageVo?>?
}