package cn.hellohao.util;

import cn.hellohao.model.entity.SiteGroup;
import cn.hellohao.model.entity.SysUser;
import cn.hellohao.service.GroupService;
import cn.hellohao.service.UserGroupService;
import cn.hellohao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 14:10
 */
@Component
public class GetCurrentSource {
    @Autowired
    private GroupService groupServiceImpl;
    @Autowired
    private UserGroupService userGroupServiceImpl;
    @Autowired
    private UserService  userServiceImpl;

    private static GroupService  groupService;
    private static UserGroupService  userGroupService;
    private static UserService userService;

    @PostConstruct
    public void init() {
        groupService =groupServiceImpl;
        userGroupService = userGroupServiceImpl;
        userService = userServiceImpl;
    }



    public static SiteGroup GetSource(String userid) {
        //UserType 0-未分配 1-游客 2-用户 3-管理员
        SysUser sysUser =null;
        if(userid!=null){
            SysUser u = new SysUser();
            u.setId(userid);
            sysUser = userService.getUsers(u);
        }
        SiteGroup siteGroup =null;
        if(sysUser ==null){
            //游客
            Integer count = groupService.getCountFroUserType(1);
            if(count>0){
                siteGroup = groupService.getGroupFroUserType(1);
            }else{
                siteGroup = groupService.getGroupListById("1");
            }
        }else{
            //用户
            if(sysUser.getGroupId()!="1"){
                //说明自定义过的优先
                siteGroup = groupService.getGroupListById(sysUser.getGroupId());
            }else{
                //默认的，用的是group主键为1的  但是还需要看看用户组有没有设置，比如管理员 用户
                if(sysUser.getLevel()>1){
                    //先查询管理员用户组有没有 如果有就用 没有就默认
                    Integer count = groupService.getCountFroUserType(3);
                    if(count>0){
                        siteGroup = groupService.getGroupFroUserType(3);
                    }else{
                        siteGroup = groupService.getGroupListById("1");
                    }
                }else{
                    //先查询普通用户组有没有 如果有就用 没有就默认
                    Integer count = groupService.getCountFroUserType(2);
                    if(count>0){
                        siteGroup = groupService.getGroupFroUserType(2);

                    }else{
                        siteGroup = groupService.getGroupListById("1");
                    }
                }
            }
        }
        return siteGroup;
    }


}
