package cn.hellohao.mapper

import cn.hellohao.model.entity.Images
import cn.hellohao.model.entity.ImgTemp
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * @Entity dao.pojo.Imgdataexp
 */
@Mapper
interface ImgTempMapper : BaseMapper<ImgTemp?> {
    fun selectDelImgUidList(@Param("datatime") datatime: String?): List<Images?>?
    fun delImgAndExp(@Param("imgUid") imgUid: String?): Int?
    fun insertImgExp(imgDataExp: ImgTemp?): Int?
}