package cn.hellohao.auth.shiro;

import cn.hellohao.model.entity.SysUser;
import cn.hellohao.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * @author Hellohao
 * @version 1.0
 * @date  2021/6/3 10:39
 * 自定义UserRealm

 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        ArrayList<String> roleList = new ArrayList();
        if(sysUser.getLevel()==2){
            roleList.add("admin");
            roleList.add("sysUser");
        }else{
            roleList.add("sysUser");
        }
        info.addRoles(roleList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken tokenOBJ) throws AuthenticationException {
        UsernamePasswordToken userToken  ;
        userToken = (UsernamePasswordToken)tokenOBJ;
        SysUser sysUser = new SysUser();
        sysUser.setEmail(userToken.getUsername());
        SysUser u = userService.getUsers(sysUser);
        if(u==null){
            return null;
        }
        //密码认证（防止泄露，不需要我们做）
        return new SimpleAuthenticationInfo(u,u.getPassword(),"");
    }
}
