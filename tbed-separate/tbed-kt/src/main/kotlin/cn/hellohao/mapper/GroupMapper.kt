package cn.hellohao.mapper

import cn.hellohao.model.entity.SiteGroup
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:11
 */
@Mapper
interface GroupMapper : BaseMapper<SiteGroup?> {
    fun grouplist(@Param("page") page: Page<SiteGroup?>?, usertype: Int?): Page<SiteGroup?>?

    /**
     * id组列表
     *
     * @param id id
     * @return [SiteGroup]
     */
    fun idGroupList(@Param("id") id: String?): SiteGroup?
    fun addgroup(siteGroup: SiteGroup?): Int?

    /**
     * 得到用户类型数
     *
     * @param usertype usertype
     * @return [Integer]
     */
    fun getUserTypeCount(@Param("usertype") usertype: Int?): Int?

    /**
     * 删除组
     *
     * @param id id
     * @return [Integer]
     */
    fun deleteGroup(@Param("id") id: String?): Int?

    /**
     * 被用户类型组
     *
     * @param usertype usertype
     * @return [SiteGroup]
     */
    fun getGroupByUserType(@Param("usertype") usertype: Int?): SiteGroup?
}