package cn.hellohao.mapper;

import cn.hellohao.model.entity.SiteGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:11
 */
@Mapper
public interface GroupMapper extends BaseMapper<SiteGroup> {
    Page<SiteGroup> grouplist(@Param("page")Page<SiteGroup> page, Integer usertype);

    /**
     * id组列表
     *
     * @param id id
     * @return {@link SiteGroup}
     */
    SiteGroup idGroupList(@Param("id") String id);
    Integer addgroup(SiteGroup siteGroup);

    /**
     * 得到用户类型数
     *
     * @param usertype usertype
     * @return {@link Integer}
     */
    Integer getUserTypeCount(@Param("usertype") Integer usertype);

    /**
     * 删除组
     *
     * @param id id
     * @return {@link Integer}
     */
    Integer deleteGroup(@Param("id") String id);

    /**
     * 被用户类型组
     *
     * @param usertype usertype
     * @return {@link SiteGroup}
     */
    SiteGroup getGroupByUserType(@Param("usertype") Integer usertype);
}
