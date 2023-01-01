package cn.hellohao.mapper;

import cn.hellohao.model.entity.UserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 13:45
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {
    /**
     * 用户组用户id
     *
     * @param userid 用户标识
     * @return {@link UserGroup}
     */
    UserGroup getUserGroupByUserId(@Param("userid") Integer userid);

    /**
     * 得到用户组id
     *
     * @param id id
     * @return {@link UserGroup}
     */
    UserGroup getUserGroupById(@Param("id") Integer id);

    /**
     * 添加用户组
     *
     * @param userGroup 用户组
     * @return {@link Integer}
     */
    Integer addUserGroup(UserGroup userGroup);

    /**
     * 更新用户组
     *
     * @param userGroup 用户组
     * @return {@link Integer}
     */
    Integer updateUserGroup(UserGroup userGroup);

    /**
     * 设置默认用户组
     *
     * @param groupid groupid
     * @return {@link Integer}
     */
    Integer setDefaultUserGroup(@Param("groupid") Integer groupid);

    /**
     * 德尔用户组
     *
     * @param userid 用户标识
     * @return {@link Integer}
     */
    Integer delUserGroup(@Param("userid") Integer userid);



}
