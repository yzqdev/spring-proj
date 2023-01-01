package cn.hellohao.mapper;

import cn.hellohao.model.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    //注册
    Integer register(SysUser sysUser);

    //登录
    Integer login(@Param("email") String email, @Param("password") String password,@Param("uid") String uid);

    //获取用户信息
    SysUser getUsers(SysUser sysUser);



    //修改资料
    Integer change(SysUser sysUser);

    Integer changeUser(SysUser sysUser);

    /**
     * 检查用户名是否重复
     *
     * @param username 用户名
     * @return {@link Integer}
     */
    Integer checkUsername(@Param("username") String username);

    Integer getUserTotal();

    Page<SysUser> getUserList(@Param("page") Page<SysUser> page, String username);


    /**
     * 刪除用戶
     *
     * @param id id
     * @return {@link Integer}
     */
    Integer deleuser(@Param("id") String id);


    /**
     * 查询用户名或者邮箱是否存在
     *
     * @param username 用户名
     * @return {@link Integer}
     */
    Integer countusername(@Param("username") String username);

    Integer countmail(@Param("email") String email);

    /**
     * 获取用户uid
     *
     * @param uid uid
     * @return {@link Integer}
     */
    Integer getUserByUid(@Param("uid") String uid);

    SysUser getUsersMail(@Param("uid") String uid);
    Integer setIsok(SysUser sysUser);

    Integer setmemory(SysUser sysUser);

    /**
     * 得到用户id
     *
     * @param id id
     * @return {@link SysUser}
     */
    SysUser getUserById(@Param("id") String id);

    List<SysUser> getUserListFromGroupId(@Param("groupid") String groupid);

}
