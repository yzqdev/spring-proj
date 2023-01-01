package cn.hellohao.service;

import cn.hellohao.model.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService extends IService<SysUser> {
    //注册
    Integer register(SysUser sysUser);

    //登录
    Integer login(String email, String password,String uid);

    //获取用户信息
    SysUser getUsers(SysUser sysUser);



    //修改资料
    Integer change(SysUser sysUser);

    Integer changeUser(SysUser sysUser);

    //检查用户名是否重复
    Integer checkUsername(String username);

    Integer getUserTotal();

    Page<SysUser> getUserListByName(Page<SysUser> page, String username);

    Integer deleteUserById(String id);

    //查询用户名或者邮箱是否存在
    Integer countusername(String username);

    Integer countmail(String email);

    Integer getUserByUid(String uid);

    SysUser getUsersMail(String uid);
    Integer setisok (SysUser sysUser);
    Integer setmemory(SysUser sysUser);
    SysUser getUsersid(String id);Integer usersetmemory(SysUser sysUser, String codestring);
    List<SysUser> getuserlistforgroupid(String groupid);
}
